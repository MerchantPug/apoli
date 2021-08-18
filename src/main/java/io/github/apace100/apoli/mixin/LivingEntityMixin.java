package io.github.apace100.apoli.mixin;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.common.power.EffectImmunityPower;
import dev.experimental.apoli.common.power.EntityGroupPower;
import dev.experimental.apoli.common.power.ModifyFallingPower;
import dev.experimental.apoli.common.power.configuration.ModifyFallingConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import io.github.apace100.apoli.Apoli;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "canStandOnFluid", at = @At("HEAD"), cancellable = true)
	private void modifyWalkableFluids(Fluid fluid, CallbackInfoReturnable<Boolean> info) {
		if (IPowerContainer.getPowers(this, ModPowers.WALK_ON_FLUID.get()).stream().anyMatch(p -> fluid.is(p.getConfiguration().value()))) {
			info.setReturnValue(true);
		}
	}

	// ModifyLavaSpeedPower
	@ModifyConstant(method = "travel", constant = {
			@Constant(doubleValue = 0.5D, ordinal = 0),
			@Constant(doubleValue = 0.5D, ordinal = 1),
			@Constant(doubleValue = 0.5D, ordinal = 2)
	})
	private double modifyLavaSpeed(double original) {
		return IPowerContainer.modify(this, ModPowers.MODIFY_LAVA_SPEED.get(), original);
	}

	@Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWaterRainOrBubble()Z"))
	private boolean preventExtinguishingFromSwimming(LivingEntity livingEntity) {
		if (IPowerContainer.hasPower(livingEntity, ModPowers.SWIMMING.get()) && livingEntity.isSwimming() && this.getFluidHeight(FluidTags.WATER) <= 0)
			return false;
		return livingEntity.isInWaterRainOrBubble();
	}

	// SetEntityGroupPower
	@Inject(at = @At("HEAD"), method = "getMobType", cancellable = true)
	public void getGroup(CallbackInfoReturnable<MobType> info) {
		List<ConfiguredPower<FieldConfiguration<MobType>, EntityGroupPower>> powers = IPowerContainer.getPowers(this, ModPowers.ENTITY_GROUP.get());
		if (powers.size() > 0) {
			if (powers.size() > 1) {
				Apoli.LOGGER.warn("Entity " + this.getDisplayName() + " has two instances of SetEntityGroupPower.");
			}
			info.setReturnValue(powers.get(0).getConfiguration().value());
		}
	}

	// HOTBLOODED
	@Inject(at = @At("HEAD"), method = "canBeAffected", cancellable = true)
	private void preventStatusEffects(MobEffectInstance effect, CallbackInfoReturnable<Boolean> info) {
		if (EffectImmunityPower.isImmune((LivingEntity) (Entity) this, effect))
			info.setReturnValue(false);
	}

	// SLOW_FALLING
	@ModifyVariable(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"), method = "travel", ordinal = 0)
	public double modifyFallingVelocity(double in) {
		List<ConfiguredPower<ModifyFallingConfiguration, ModifyFallingPower>> modifyFallingPowers = IPowerContainer.getPowers(this, ModPowers.MODIFY_FALLING.get());
		//FIXME Use forge gravity
		if (modifyFallingPowers.size() > 0) {
			ConfiguredPower<ModifyFallingConfiguration, ModifyFallingPower> power = modifyFallingPowers.get(0);
			if (!power.getConfiguration().takeFallDamage()) {
				this.fallDistance = 0;
			}
			if (this.getDeltaMovement().y <= 0.0D) {
				return power.getConfiguration().velocity();
			}
		}
		return in;
	}
}
