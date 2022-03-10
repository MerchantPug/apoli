package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.access.ModifiableFoodEntity;
import io.github.apace100.apoli.power.ActionOnHitPower;
import io.github.apace100.apoli.power.ActionWhenHitPower;
import io.github.apace100.apoli.power.PreventEntityCollisionPower;
import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.power.EntityGroupPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyFallingPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyFoodPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyFoodConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableObject;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ModifiableFoodEntity {

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "canStandOnFluid", at = @At("HEAD"), cancellable = true)
	private void modifyWalkableFluids(Fluid fluid, CallbackInfoReturnable<Boolean> info) {
		if (IPowerContainer.getPowers(this, ApoliPowers.WALK_ON_FLUID.get()).stream().anyMatch(p -> p.getConfiguration().contains(fluid))) {
			info.setReturnValue(true);
		}
	}
   /* @Inject(method = "onEffectAdded", at = @At("TAIL"))
    private void updateStatusEffectWhenApplied(StatusEffectInstance effectInstance, Entity source, CallbackInfo ci) {
        SyncStatusEffectsUtil.sendStatusEffectUpdatePacket((LivingEntity)(Object)this, SyncStatusEffectsUtil.UpdateType.APPLY, effectInstance);
    }

    @Inject(method = "onEffectUpdated", at = @At("TAIL"))
    private void updateStatusEffectWhenUpgraded(StatusEffectInstance effectInstance, boolean reapplyEffect, Entity source, CallbackInfo ci) {
        SyncStatusEffectsUtil.sendStatusEffectUpdatePacket((LivingEntity)(Object)this, SyncStatusEffectsUtil.UpdateType.UPGRADE, effectInstance);
    }

    @Inject(method = "onEffectRemoved", at = @At("TAIL"))
    private void updateStatusEffectWhenRemoved(StatusEffectInstance effectInstance, CallbackInfo ci) {
        SyncStatusEffectsUtil.sendStatusEffectUpdatePacket((LivingEntity)(Object)this, SyncStatusEffectsUtil.UpdateType.REMOVE, effectInstance);
    }

    @Inject(method = "clearStatusEffects", at = @At("RETURN"))
    private void updateStatusEffectWhenCleared(CallbackInfoReturnable<Boolean> cir) {
        SyncStatusEffectsUtil.sendStatusEffectUpdatePacket((LivingEntity)(Object)this, SyncStatusEffectsUtil.UpdateType.CLEAR, null);
    }*/

	@ModifyVariable(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"))
	private StatusEffectInstance modifyStatusEffect(StatusEffectInstance effect) {
		StatusEffect effectType = effect.getEffectType();
		int originalAmp = effect.getAmplifier();
		int originalDur = effect.getDuration();

		int amplifier = Math.round(PowerHolderComponent.modify(this, ModifyStatusEffectAmplifierPower.class, originalAmp, power -> power.doesApply(effectType)));
		int duration = Math.round(PowerHolderComponent.modify(this, ModifyStatusEffectDurationPower.class, originalDur, power -> power.doesApply(effectType)));

		if (amplifier != originalAmp || duration != originalDur) {
			return new StatusEffectInstance(
					effectType,
					duration,
					amplifier,
					effect.isAmbient(),
					effect.shouldShowParticles(),
					effect.shouldShowIcon(),
					((HiddenEffectStatus) effect).getHiddenEffect()
			);
		}
		return effect;
	}

    /*@Inject(method = "setAttacker", at = @At("TAIL"))
    private void syncAttacker(LivingEntity attacker, CallbackInfo ci) {
        if(!world.isClient) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(this.getId());
            if(this.attacker == null) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                buf.writeInt(this.attacker.getId());
            }
            for (ServerPlayerEntity player : PlayerLookup.tracking(this)) {
                ServerPlayNetworking.send(player, ModPackets.SET_ATTACKER, buf);
            }
        }
    }*/

	@Inject(method = "collectEquipmentChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeMap;removeAttributeModifiers(Lcom/google/common/collect/Multimap;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void removeEquipmentPowers(CallbackInfoReturnable<Map> cir, Map map, EquipmentSlot[] var2, int var3, int var4, EquipmentSlot equipmentSlot, ItemStack itemStack3, ItemStack itemStack4) {
		List<StackPowerUtil.StackPower> powers = StackPowerUtil.getPowers(itemStack3, equipmentSlot);
		if (powers.size() > 0) {
			Identifier source = new Identifier(Apoli.MODID, equipmentSlot.getName());
			PowerHolderComponent powerHolder = PowerHolderComponent.KEY.get(this);
			powers.forEach(sp -> {
				if (PowerTypeRegistry.contains(sp.powerId)) {
					powerHolder.removePower(PowerTypeRegistry.get(sp.powerId), source);
				}
			});
			powerHolder.sync();
		}
	}

	@Inject(method = "collectEquipmentChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeMap;addTransientAttributeModifiers(Lcom/google/common/collect/Multimap;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void addEquipmentPowers(CallbackInfoReturnable<Map> cir, Map map, EquipmentSlot[] var2, int var3, int var4, EquipmentSlot equipmentSlot, ItemStack itemStack3, ItemStack itemStack4) {
		List<StackPowerUtil.StackPower> powers = StackPowerUtil.getPowers(itemStack4, equipmentSlot);
		if (powers.size() > 0) {
			Identifier source = new Identifier(Apoli.MODID, equipmentSlot.getName());
			PowerHolderComponent powerHolder = PowerHolderComponent.KEY.get(this);
			powers.forEach(sp -> {
				if (PowerTypeRegistry.contains(sp.powerId)) {
					powerHolder.addPower(PowerTypeRegistry.get(sp.powerId), source);
				}
			});
			powerHolder.sync();
		} else if (StackPowerUtil.getPowers(itemStack3, equipmentSlot).size() > 0) {
			PowerHolderComponent.KEY.get(this).sync();
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

	@Inject(method = "damage", at = @At("RETURN"))
	private void invokeHitActions(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			Entity attacker = source.getAttacker();
			if (attacker != null) {
				PowerHolderComponent.withPowers(this, ActionWhenHitPower.class, p -> true, p -> p.whenHit(attacker, source, amount));
				PowerHolderComponent.withPowers(attacker, ActionOnHitPower.class, p -> true, p -> p.onHit(this, source, amount));
			}
		}
	}

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V"))
	private void invokeKillAction(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		PowerHolderComponent.getPowers(source.getAttacker(), SelfActionOnKillPower.class).forEach(p -> p.onKill((LivingEntity) (Object) this, source, amount));
	}

	@Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWaterRainOrBubble()Z"))
	private boolean preventExtinguishingFromSwimming(LivingEntity livingEntity) {
		if (IPowerContainer.hasPower(livingEntity, ApoliPowers.SWIMMING.get()) && livingEntity.isSwimming() && this.getFluidHeight(FluidTags.WATER) <= 0)
			return false;
		return livingEntity.isInWaterRainOrBubble();
	}

	@Unique
	private boolean prevPowderSnowState = false;

	@Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getTicksFrozen()I"))
	private void freezeEntityFromPower(CallbackInfo ci) {
		if (IPowerContainer.hasPower(this, ApoliPowers.FREEZE.get())) {
			this.prevPowderSnowState = this.isInPowderSnow;
			this.isInPowderSnow = true;
		}
	}

	@Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeFrost()V"))
	private void unfreezeEntityFromPower(CallbackInfo ci) {
		if (IPowerContainer.hasPower(this, ApoliPowers.FREEZE.get()))
			this.isInPowderSnow = this.prevPowderSnowState;
	}

	@Inject(method = "canFreeze", at = @At("RETURN"), cancellable = true)
	private void allowFreezingPower(CallbackInfoReturnable<Boolean> cir) {
		if (IPowerContainer.hasPower(this, ApoliPowers.FREEZE.get()))
			cir.setReturnValue(true);
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

	@ModifyVariable(method = "travel", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;onGround:Z", opcode = Opcodes.GETFIELD, ordinal = 2))
	private float modifySlipperiness(float original) {
		return PowerHolderComponent.modify(this, ModifySlipperinessPower.class, original, p -> p.doesApply(world, getVelocityAffectingPos()));
	}

	@Inject(method = "doPush", at = @At("HEAD"), cancellable = true)
	private void preventPushing(Entity entity, CallbackInfo ci) {
		if (PowerHolderComponent.hasPower(this, PreventEntityCollisionPower.class, p -> p.doesApply(entity))
			|| PowerHolderComponent.hasPower(entity, PreventEntityCollisionPower.class, p -> p.doesApply(this))) {
			ci.cancel();
		}
	}

	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;getValue()D", ordinal = 0))
	public void modifyFall(Vec3 motion, CallbackInfo ci) {
		ModifyFallingPower.apply((Entity) this, this.getDeltaMovement().y <= 0.0D);
	}

	@ModifyVariable(method = "eat", at = @At("HEAD"), argsOnly = true)
	private ItemStack modifyEatenItemStack(ItemStack original) {
		if ((Object) this instanceof Player) {
			return original;
		}
		List<ConfiguredPower<ModifyFoodConfiguration, ModifyFoodPower>> mfps = ModifyFoodPower.getValidPowers(this, original);
		MutableObject<ItemStack> stack = new MutableObject<>(original.copy());
		ModifyFoodPower.modifyStack(mfps, this.level, stack);
		((ModifiableFoodEntity) this).setCurrentModifyFoodPowers(mfps);
		((ModifiableFoodEntity) this).setOriginalFoodStack(original);
		return stack.getValue();
	}

	@ModifyVariable(method = "eat",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEatEffect(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V", shift = At.Shift.AFTER),
			argsOnly = true)
	private ItemStack unmodifyEatenItemStack(ItemStack modified) {
		ModifiableFoodEntity foodEntity = this;
		ItemStack original = foodEntity.getOriginalFoodStack();
		if (original != null) {
			foodEntity.setOriginalFoodStack(null);
			return original;
		}
		return modified;
	}

	@Inject(method = "eat", at = @At("TAIL"))
	private void removeCurrentModifyFoodPowers(Level world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		this.setCurrentModifyFoodPowers(new LinkedList<>());
	}

	@Redirect(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEatEffect(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V"))
	private void preventApplyingFoodEffects(LivingEntity livingEntity, ItemStack stack, Level world, LivingEntity targetEntity) {
		if (this.getCurrentModifyFoodPowers().stream().anyMatch(x -> x.getConfiguration().preventEffects()))
			return;
		this.addEatEffect(stack, world, targetEntity);
	}

	@Shadow
	@Nullable
	private LivingEntity attacker;

	@Shadow
	protected abstract void addEatEffect(ItemStack p_21064_, Level p_21065_, LivingEntity p_21066_);

	@Shadow
	public float flyingSpeed;

	@Inject(method = "getFrictionInfluencedSpeed(F)F", at = @At("RETURN"), cancellable = true)
	private void modifyFlySpeed(float slipperiness, CallbackInfoReturnable<Float> cir) {
		if (!this.onGround)
			cir.setReturnValue(IPowerContainer.modify(this, ApoliPowers.MODIFY_AIR_SPEED.get(), this.flyingSpeed));
	}

	@Unique
	private List<ConfiguredPower<ModifyFoodConfiguration, ModifyFoodPower>> apoli$currentModifyFoodPowers = new LinkedList<>();

	@Unique
	private ItemStack apoli$originalFoodStack;

	@Override
	public List<ConfiguredPower<ModifyFoodConfiguration, ModifyFoodPower>> getCurrentModifyFoodPowers() {
		return this.apoli$currentModifyFoodPowers;
	}

	@Override
	public void setCurrentModifyFoodPowers(List<ConfiguredPower<ModifyFoodConfiguration, ModifyFoodPower>> powers) {
		this.apoli$currentModifyFoodPowers = powers;
	}

	@Override
	public ItemStack getOriginalFoodStack() {
		return this.apoli$originalFoodStack;
	}

	@Override
	public void setOriginalFoodStack(ItemStack original) {
		this.apoli$originalFoodStack = original;
	}
}
