package io.github.apace100.apoli.mixin;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityAction;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.common.ApoliCommon;
import dev.experimental.apoli.common.network.C2SPlayerLandedPacket;
import dev.experimental.apoli.common.power.ActionOnLandPower;
import dev.experimental.apoli.common.power.InvulnerablePower;
import dev.experimental.apoli.common.power.PhasingPower;
import dev.experimental.apoli.common.registry.ModPowers;
import io.github.apace100.apoli.access.MovingEntity;
import io.github.apace100.apoli.access.SubmergableEntity;
import io.github.apace100.apoli.access.WaterMovingEntity;
import io.github.apace100.calio.Calio;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
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

	@Shadow
	public Level level;
	@Shadow
	public float moveDist;
	@Shadow
	protected boolean onGround;
	@Shadow
	@Nullable
	protected Tag<Fluid> fluidOnEyes;
	@Shadow
	protected Object2DoubleMap<Tag<Fluid>> fluidHeight;
	@Unique
	private boolean wasGrounded = false;
	private boolean isMoving;
	private float distanceBefore;

	@Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
	private void makeFullyFireImmune(CallbackInfoReturnable<Boolean> cir) {
		if (IPowerContainer.hasPower((Entity) (Object) this, ModPowers.FIRE_IMMUNITY.get())) {
			cir.setReturnValue(true);
		}
	}

	@Shadow
	public abstract double getFluidHeight(Tag<Fluid> fluid);

	@Inject(method = "isInWater", at = @At("HEAD"), cancellable = true)
	private void makeEntitiesIgnoreWater(CallbackInfoReturnable<Boolean> cir) {
		if (IPowerContainer.hasPower((Entity) (Object) this, ModPowers.IGNORE_WATER.get())) {
			if (this instanceof WaterMovingEntity) {
				if (((WaterMovingEntity) this).isInMovementPhase()) {
					cir.setReturnValue(false);
				}
			}
		}
	}

	@Inject(method = "move", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V", args = {"ldc=rest"}))
	private void checkWasGrounded(MoverType type, Vec3 movement, CallbackInfo ci) {
		this.wasGrounded = this.onGround;
	}

	@OnlyIn(Dist.CLIENT)
	@Inject(method = "checkFallDamage", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;fallDistance:F", opcode = Opcodes.PUTFIELD, ordinal = 0))
	private void invokeActionOnSoftLand(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo ci) {
		if (!this.wasGrounded && (Object) this instanceof LocalPlayer player) {
			List<ConfiguredPower<FieldConfiguration<ConfiguredEntityAction<?, ?>>, ActionOnLandPower>> powers = IPowerContainer.getPowers(player, ModPowers.ACTION_ON_LAND.get());
			powers.forEach(x -> x.getFactory().executeAction(x, player));
			if (powers.size() > 0)
				ApoliCommon.CHANNEL.send(PacketDistributor.SERVER.noArg(), new C2SPlayerLandedPacket());
		}
	}

	@Inject(at = @At("HEAD"), method = "isInvulnerableTo", cancellable = true)
	private void makeOriginInvulnerable(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof LivingEntity living && InvulnerablePower.isInvulnerableTo(living, damageSource))
			cir.setReturnValue(true);
	}

	@Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInWaterRainOrBubble()Z"))
	private boolean preventExtinguishingFromSwimming(Entity entity) {
		if (IPowerContainer.hasPower(entity, ModPowers.SWIMMING.get()) && entity.isSwimming() && !(this.getFluidHeight(FluidTags.WATER) > 0))
			return false;
		return entity.isInWaterRainOrBubble();
	}

	@Inject(at = @At("HEAD"), method = "isInvisible", cancellable = true)
	private void phantomInvisibility(CallbackInfoReturnable<Boolean> info) {
		if (IPowerContainer.hasPower((Entity) (Object) this, ModPowers.INVISIBILITY.get()))
			info.setReturnValue(true);
	}

	@Inject(at = @At(value = "HEAD"), method = "moveTowardsClosestSpace", cancellable = true)
	protected void pushOutOfBlocks(double x, double y, double z, CallbackInfo info) {
		if (((Object) this) instanceof LivingEntity le && PhasingPower.shouldPhaseThrough(le, new BlockPos(x, y, z)))
			info.cancel();
	}

	@Inject(method = "move", at = @At("HEAD"))
	private void saveDistanceTraveled(MoverType type, Vec3 movement, CallbackInfo ci) {
		this.isMoving = false;
		this.distanceBefore = this.moveDist;
	}

	@Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V"))
	private void checkIsMoving(MoverType type, Vec3 movement, CallbackInfo ci) {
		if (this.moveDist > this.distanceBefore)
			this.isMoving = true;
	}

	@Override
	public boolean isSubmergedInLoosely(Tag<Fluid> tag) {
		if (tag == null || this.fluidOnEyes == null) {
			return false;
		}
		if (tag == this.fluidOnEyes) {
			return true;
		}
		return Calio.areTagsEqual(Registry.FLUID_REGISTRY, tag, this.fluidOnEyes);
	}

	@Override
	public double getFluidHeightLoosely(Tag<Fluid> tag) {
		if (tag == null) {
			return 0;
		}
		if (this.fluidHeight.containsKey(tag)) {
			return this.fluidHeight.getDouble(tag);
		}
		for (Tag<Fluid> ft : this.fluidHeight.keySet()) {
			if (Calio.areTagsEqual(Registry.FLUID_REGISTRY, ft, tag)) {
				return this.fluidHeight.getDouble(ft);
			}
		}
		return 0;
	}

	@Override
	public boolean isMoving() {
		return this.isMoving;
	}
}
