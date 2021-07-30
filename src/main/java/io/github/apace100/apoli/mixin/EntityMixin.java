package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.access.MovingEntity;
import io.github.apace100.apoli.access.SubmergableEntity;
import io.github.apace100.apoli.access.WaterMovingEntity;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.networking.ModPackets;
import io.github.apace100.apoli.power.*;
import io.github.apace100.calio.Calio;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin implements MovingEntity, SubmergableEntity {

    @Inject(method = "isFireImmune", at = @At("HEAD"), cancellable = true)
    private void makeFullyFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if(PowerHolderComponent.hasPower((Entity)(Object)this, FireImmunityPower.class)) {
            cir.setReturnValue(true);
        }
    }

    @Shadow
    public Level world;

    @Shadow
    public abstract double getFluidHeight(Tag<Fluid> fluid);

    @Shadow
    public abstract Vec3 getVelocity();

    @Shadow
    public float distanceTraveled;

    @Shadow
    protected boolean onGround;

    @Shadow @Nullable protected Tag<Fluid> submergedFluidTag;

    @Shadow protected Object2DoubleMap<Tag<Fluid>> fluidHeight;

    @Inject(method = "isTouchingWater", at = @At("HEAD"), cancellable = true)
    private void makeEntitiesIgnoreWater(CallbackInfoReturnable<Boolean> cir) {
        if(PowerHolderComponent.hasPower((Entity)(Object)this, IgnoreWaterPower.class)) {
            if(this instanceof WaterMovingEntity) {
                if(((WaterMovingEntity)this).isInMovementPhase()) {
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Unique
    private boolean wasGrounded = false;

    @Inject(method = "move", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", args = {"ldc=rest"}))
    private void checkWasGrounded(MoverType type, Vec3 movement, CallbackInfo ci) {
        wasGrounded = this.onGround;
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "fall", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;fallDistance:F", opcode = Opcodes.PUTFIELD, ordinal = 0))
    private void invokeActionOnSoftLand(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo ci) {
        if(!wasGrounded && (Object)this instanceof LocalPlayer) {
            List<ActionOnLandPower> powers = PowerHolderComponent.getPowers((Entity)(Object)this, ActionOnLandPower.class);
            powers.forEach(ActionOnLandPower::executeAction);
            if(powers.size() > 0) {
                ClientPlayNetworking.send(ModPackets.PLAYER_LANDED, PacketByteBufs.create());
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "isInvulnerableTo", cancellable = true)
    private void makeOriginInvulnerable(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if((Object)this instanceof LivingEntity) {
            PowerHolderComponent component = PowerHolderComponent.KEY.get(this);
            if(component.getPowers(InvulnerablePower.class).stream().anyMatch(inv -> inv.doesApply(damageSource))) {
                cir.setReturnValue(true);
            }
        }
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isWet()Z"))
    private boolean preventExtinguishingFromSwimming(Entity entity) {
        if(PowerHolderComponent.hasPower(entity, SwimmingPower.class) && entity.isSwimming() && !(getFluidHeight(FluidTags.WATER) > 0)) {
            return false;
        }
        return entity.isInWaterRainOrBubble();
    }

    @Inject(at = @At("HEAD"), method = "isInvisible", cancellable = true)
    private void phantomInvisibility(CallbackInfoReturnable<Boolean> info) {
        if(PowerHolderComponent.hasPower((Entity)(Object)this, InvisibilityPower.class)) {
            info.setReturnValue(true);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;<init>(DDD)V"), method = "pushOutOfBlocks", cancellable = true)
    protected void pushOutOfBlocks(double x, double y, double z, CallbackInfo info) {
        List<PhasingPower> powers = PowerHolderComponent.getPowers((Entity)(Object)this, PhasingPower.class);
        if(powers.size() > 0) {
            if(powers.stream().anyMatch(phasingPower -> phasingPower.doesApply(new BlockPos(x, y, z)))) {
                info.cancel();
            }
        }
    }

    private boolean isMoving;
    private float distanceBefore;

    @Inject(method = "move", at = @At("HEAD"))
    private void saveDistanceTraveled(MoverType type, Vec3 movement, CallbackInfo ci) {
        this.isMoving = false;
        this.distanceBefore = this.distanceTraveled;
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
    private void checkIsMoving(MoverType type, Vec3 movement, CallbackInfo ci) {
        if(this.distanceTraveled > this.distanceBefore) {
            this.isMoving = true;
        }
    }

    @Override
    public boolean isSubmergedInLoosely(Tag<Fluid> tag) {
        if(tag == null || submergedFluidTag == null) {
            return false;
        }
        if(tag == submergedFluidTag) {
            return true;
        }
        return Calio.areTagsEqual(Registry.FLUID_REGISTRY, tag, submergedFluidTag);
    }

    @Override
    public double getFluidHeightLoosely(Tag<Fluid> tag) {
        if(tag == null) {
            return 0;
        }
        if(fluidHeight.containsKey(tag)) {
            return fluidHeight.getDouble(tag);
        }
        for(Tag<Fluid> ft : fluidHeight.keySet()) {
            if(Calio.areTagsEqual(Registry.FLUID_REGISTRY, ft, tag)) {
                return fluidHeight.getDouble(ft);
            }
        }
        return 0;
    }

    @Override
    public boolean isMoving() {
        return isMoving;
    }
}
