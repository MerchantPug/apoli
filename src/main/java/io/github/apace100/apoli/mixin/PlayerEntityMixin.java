package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.*;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.entity.*;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Nameable, CommandSource {

    @Shadow
    public abstract boolean hurt(DamageSource source, float amount);

    @Shadow
    public abstract EntityDimensions getDimensions(Pose pose);

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    @Shadow
    protected boolean isSubmergedInWater;

    @Shadow
    @Final
    public Inventory inventory;

    @Shadow
    public abstract ItemEntity dropItem(ItemStack stack, boolean retainOwnership);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "updateSwimming", at = @At("TAIL"))
    private void updateSwimmingPower(CallbackInfo ci) {
        if(PowerHolderComponent.hasPower(this, SwimmingPower.class)) {
            this.setSwimming(this.isSprinting() && !this.isPassenger());
            this.wasTouchingWater = this.isSwimming();
            if (this.isSwimming()) {
                this.fallDistance = 0.0F;
                Vec3 look = this.getLookAngle();
                move(MoverType.SELF, new Vec3(look.x/4, look.y/4, look.z/4));
            }
        } else if(PowerHolderComponent.hasPower(this, IgnoreWaterPower.class)) {
            this.setSwimming(false);
        }
    }

    @Inject(method = "wakeUp(ZZ)V", at = @At("HEAD"))
    private void invokeWakeUpAction(boolean bl, boolean updateSleepingPlayers, CallbackInfo ci) {
        if(!bl && !updateSleepingPlayers && getSleepingPos().isPresent()) {
            BlockPos sleepingPos = getSleepingPos().get();
            PowerHolderComponent.getPowers(this, ActionOnWakeUp.class).stream().filter(p -> p.doesApply(sleepingPos)).forEach(p -> p.executeActions(sleepingPos, Direction.DOWN));
        }
    }

    // Prevent healing if DisableRegenPower
    // Note that this function was called "shouldHeal" instead of "canFoodHeal" at some point in time.
    @Inject(method = "canFoodHeal", at = @At("HEAD"), cancellable = true)
    private void disableHeal(CallbackInfoReturnable<Boolean> info) {
        if(PowerHolderComponent.hasPower(this, DisableRegenPower.class)) {
            info.setReturnValue(false);
        }
    }

    // ModifyExhaustion
    @ModifyVariable(at = @At("HEAD"), method = "addExhaustion", ordinal = 0, name = "exhaustion")
    private float modifyExhaustion(float exhaustionIn) {
        return PowerHolderComponent.modify(this, ModifyExhaustionPower.class, exhaustionIn);
    }

    // ModifyDamageDealt
    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 0), name = "f", ordinal = 0)
    public float modifyDamage(float f, Entity target) {
        DamageSource source = DamageSource.playerAttack((Player)(Object)this);
        return PowerHolderComponent.modify(this, ModifyDamageDealtPower.class, f, p -> p.doesApply(source, f, target instanceof LivingEntity ? (LivingEntity)target : null), p -> p.executeActions(target));
    }

    // NO_COBWEB_SLOWDOWN
    /*@Inject(at = @At("HEAD"), method = "slowMovement", cancellable = true)
    public void slowMovement(BlockState state, Vec3d multiplier, CallbackInfo info) {
        if (PowerTypes.NO_COBWEB_SLOWDOWN.isActive(this) || PowerTypes.MASTER_OF_WEBS_NO_SLOWDOWN.isActive(this)) {
            info.cancel();
        }
    }

    // AQUA_AFFINITY
    @ModifyConstant(method = "getBlockBreakingSpeed", constant = @Constant(ordinal = 0, floatValue = 5.0F))
    private float modifyWaterBlockBreakingSpeed(float in) {
        if(PowerTypes.AQUA_AFFINITY.isActive(this)) {
            return 1F;
        }
        return in;
    }

    // AQUA_AFFINITY
    @ModifyConstant(method = "getBlockBreakingSpeed", constant = @Constant(ordinal = 1, floatValue = 5.0F))
    private float modifyUngroundedBlockBreakingSpeed(float in) {
        if(this.isInsideWaterOrBubbleColumn() && PowerTypes.AQUA_AFFINITY.isActive(this)) {
            return 1F;
        }
        return in;
    }*/

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
    private void dropAdditionalInventory(CallbackInfo ci) {
        PowerHolderComponent.getPowers(this, InventoryPower.class).forEach(inventory -> {
            if(inventory.shouldDropOnDeath()) {
                for(int i = 0; i < inventory.getContainerSize(); ++i) {
                    ItemStack itemStack = inventory.getItem(i);
                    if(inventory.shouldDropOnDeath(itemStack)) {
                        if (!itemStack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemStack)) {
                            inventory.removeItemNoUpdate(i);
                        } else {
                            ((Player)(Object)this).drop(itemStack, true, false);
                            inventory.setItem(i, ItemStack.EMPTY);
                        }
                    }
                }
            }
        });
    }

    @Inject(method = "canEquip", at = @At("HEAD"), cancellable = true)
    private void preventArmorDispensing(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        EquipmentSlot slot = Mob.getEquipmentSlotForItem(stack);
        PowerHolderComponent component = PowerHolderComponent.KEY.get(this);
        if(component.getPowers(RestrictArmorPower.class).stream().anyMatch(rap -> !rap.canEquip(stack, slot))) {
            info.setReturnValue(false);
        }
        if(stack.getItem() == Items.ELYTRA && PowerHolderComponent.getPowers(this, ElytraFlightPower.class).size() > 0) {
            info.setReturnValue(false);
        }
    }
    /*
    // WATER_BREATHING
    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo info) {
        if(PowerTypes.WATER_BREATHING.isActive(this)) {
            if(!this.isSubmergedIn(FluidTags.WATER) && !this.hasStatusEffect(StatusEffects.WATER_BREATHING) && !this.hasStatusEffect(StatusEffects.CONDUIT_POWER)) {
                if(!this.isRainingAtPlayerPosition()) {
                    int landGain = this.getNextAirOnLand(0);
                    this.setAir(this.getNextAirUnderwater(this.getAir()) - landGain);
                    if (this.getAir() == -20) {
                        this.setAir(0);

                        for(int i = 0; i < 8; ++i) {
                            double f = this.random.nextDouble() - this.random.nextDouble();
                            double g = this.random.nextDouble() - this.random.nextDouble();
                            double h = this.random.nextDouble() - this.random.nextDouble();
                            this.world.addParticle(ParticleTypes.BUBBLE, this.getParticleX(0.5), this.getEyeY() + this.random.nextGaussian() * 0.08D, this.getParticleZ(0.5), f * 0.5F, g * 0.5F + 0.25F, h * 0.5F);
                        }

                        this.damage(ModDamageSources.NO_WATER_FOR_GILLS, 2.0F);
                    }
                } else {
                    int landGain = this.getNextAirOnLand(0);
                    this.setAir(this.getAir() - landGain);
                }
            } else if(this.getAir() < this.getMaxAir()){
                this.setAir(this.getNextAirOnLand(this.getAir()));
            }
        }
    }


    // WATER_BREATHING
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSubmergedIn(Lnet/minecraft/tag/Tag;)Z"), method = "updateTurtleHelmet")
    public boolean isSubmergedInProxy(PlayerEntity player, Tag<Fluid> fluidTag) {
        boolean submerged = this.isSubmergedIn(fluidTag);
        if(PowerTypes.WATER_BREATHING.isActive(this)) {
            return !submerged;
        }
        return submerged;
    }*/
/*
    // WEBBING
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;onAttacking(Lnet/minecraft/entity/Entity;)V"), method = "attack")
    public void cobwebOnMeleeAttack(Entity target, CallbackInfo info) {
        if(target instanceof LivingEntity) {
            if(PowerTypes.WEBBING.isActive(this) && !this.isSneaking()) {
                CooldownPower power = PowerTypes.WEBBING.get(this);
                if(power.canUse()) {
                    BlockPos targetPos = target.getBlockPos();
                    if(world.isAir(targetPos) || world.getBlockState(targetPos).getMaterial().isReplaceable()) {
                        world.setBlockState(targetPos, ModBlocks.TEMPORARY_COBWEB.getDefaultState());
                        power.use();
                    }
                }
            }
        }
    }*/
}
