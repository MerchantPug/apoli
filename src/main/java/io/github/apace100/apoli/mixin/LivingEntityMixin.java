package io.github.apace100.apoli.mixin;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.power.EffectImmunityPower;
import io.github.edwinmindcraft.apoli.common.power.EntityGroupPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyFallingPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyFallingConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import io.github.apace100.apoli.Apoli;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "canStandOnFluid", at = @At("HEAD"), cancellable = true)
	private void modifyWalkableFluids(Fluid fluid, CallbackInfoReturnable<Boolean> info) {
		if (IPowerContainer.getPowers(this, ApoliPowers.WALK_ON_FLUID.get()).stream().anyMatch(p -> fluid.is(p.getConfiguration().value()))) {
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
		return IPowerContainer.modify(this, ApoliPowers.MODIFY_LAVA_SPEED.get(), original);
	}

	@Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWaterRainOrBubble()Z"))
	private boolean preventExtinguishingFromSwimming(LivingEntity livingEntity) {
		if (IPowerContainer.hasPower(livingEntity, ApoliPowers.SWIMMING.get()) && livingEntity.isSwimming() && this.getFluidHeight(FluidTags.WATER) <= 0)
			return false;
		return livingEntity.isInWaterRainOrBubble();
	}

	// SetEntityGroupPower
	@Inject(at = @At("HEAD"), method = "getMobType", cancellable = true)
	public void getGroup(CallbackInfoReturnable<MobType> info) {
		List<ConfiguredPower<FieldConfiguration<MobType>, EntityGroupPower>> powers = IPowerContainer.getPowers(this, ApoliPowers.ENTITY_GROUP.get());
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

	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;getValue()D", ordinal = 0))
	public void modifyFall(Vec3 motion, CallbackInfo ci) {
		ModifyFallingPower.apply((LivingEntity) (Entity) this, this.getDeltaMovement().y <= 0.0D);
	}
}
