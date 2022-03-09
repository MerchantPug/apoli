package io.github.apace100.apoli.mixin;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import io.github.edwinmindcraft.apoli.common.util.CoreUtils;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.Container;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Nameable, CommandSource {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Shadow
	public abstract boolean hurt(@NotNull DamageSource source, float amount);

	@Shadow
	public abstract @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot slot);

	@Shadow
	public abstract @NotNull EntityDimensions getDimensions(@NotNull Pose pose);

	@Shadow
	@Nullable
	public abstract ItemEntity drop(ItemStack p_36179_, boolean p_36180_, boolean p_36181_);

	@Inject(method = "updateSwimming", at = @At("TAIL"))
	private void updateSwimmingPower(CallbackInfo ci) {
		if (IPowerContainer.hasPower(this, ApoliPowers.SWIMMING.get())) {
			this.setSwimming(this.isSprinting() && !this.isPassenger());
			this.wasTouchingWater = this.isSwimming();
			if (this.isSwimming()) {
				this.fallDistance = 0.0F;
				Vec3 look = this.getLookAngle();
				this.move(MoverType.SELF, new Vec3(look.x / 4, look.y / 4, look.z / 4));
			}
		} else if (IPowerContainer.hasPower(this, ApoliPowers.IGNORE_WATER.get())) {
			this.setSwimming(false);
		}
	}
    @ModifyVariable(method = "eatFood", at = @At("HEAD"), argsOnly = true)
    private ItemStack modifyEatenItemStack(ItemStack original) {
        List<ModifyFoodPower> mfps = PowerHolderComponent.getPowers(this, ModifyFoodPower.class);
        mfps = mfps.stream().filter(mfp -> mfp.doesApply(original)).collect(Collectors.toList());
        ItemStack newStack = original;
        for(ModifyFoodPower mfp : mfps) {
            newStack = mfp.getConsumedItemStack(newStack);
        }
        ((ModifiableFoodEntity)this).setCurrentModifyFoodPowers(mfps);
        ((ModifiableFoodEntity)this).setOriginalFoodStack(original);
        return newStack;
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;"), index = 1)
    private double adjustVerticalSwimSpeed(double original) {
        return PowerHolderComponent.modify(this, ModifySwimSpeedPower.class, original);
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void preventEntityInteraction(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(this.isSpectator()) {
            return;
        }
        ItemStack stack = this.getStackInHand(hand);
        for(PreventEntityUsePower peup : PowerHolderComponent.getPowers(this, PreventEntityUsePower.class)) {
            if(peup.doesApply(entity, hand, stack)) {
                cir.setReturnValue(peup.executeAction(entity, hand));
                cir.cancel();
                return;
            }
        }
        for(PreventBeingUsedPower pbup : PowerHolderComponent.getPowers(entity, PreventBeingUsedPower.class)) {
            if(pbup.doesApply((PlayerEntity) (Object) this, hand, stack)) {
                cir.setReturnValue(pbup.executeAction((PlayerEntity) (Object) this, hand));
                cir.cancel();
                return;
            }
        }
        ActionResult result = ActionResult.PASS;
        List<ActionOnEntityUsePower> powers = PowerHolderComponent.getPowers(this, ActionOnEntityUsePower.class).stream().filter(p -> p.shouldExecute(entity, hand, stack)).toList();
        for (ActionOnEntityUsePower aoip : powers) {
            ActionResult ar = aoip.executeAction(entity, hand);
            if(ar.isAccepted() && !result.isAccepted()) {
                result = ar;
            } else if(ar.shouldSwingHand() && !result.shouldSwingHand()) {
                result = ar;
            }
        }
        List<ActionOnBeingUsedPower> otherPowers = PowerHolderComponent.getPowers(entity, ActionOnBeingUsedPower.class).stream()
            .filter(p -> p.shouldExecute((PlayerEntity) (Object) this, hand, stack)).collect(Collectors.toList());
        for(ActionOnBeingUsedPower awip : otherPowers) {
            ActionResult ar = awip.executeAction((PlayerEntity) (Object) this, hand);
            if(ar.isAccepted() && !result.isAccepted()) {
                result = ar;
            } else if(ar.shouldSwingHand() && !result.shouldSwingHand()) {
                result = ar;
            }
        }
        if(powers.size() > 0 || otherPowers.size() > 0) {
            cir.setReturnValue(result);
            cir.cancel();
        }
    }

    @Inject(method = "dismountVehicle", at = @At("HEAD"))
    private void sendPlayerDismountPacket(CallbackInfo ci) {
        if(!world.isClient && getVehicle() instanceof PlayerEntity) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(getId());
            ServerPlayNetworking.send((ServerPlayerEntity) getVehicle(), ModPackets.PLAYER_DISMOUNT, buf);
        }
    }

    @Inject(method = "updateSwimming", at = @At("TAIL"))
    private void updateSwimmingPower(CallbackInfo ci) {
        if(PowerHolderComponent.hasPower(this, SwimmingPower.class)) {
            this.setSwimming(this.isSprinting() && !this.hasVehicle());
            this.touchingWater = this.isSwimming();
            if (this.isSwimming()) {
                this.fallDistance = 0.0F;
                Vec3d look = this.getRotationVector();
                move(MovementType.SELF, new Vec3d(look.x/4, look.y/4, look.z/4));
            }
        } else if(PowerHolderComponent.hasPower(this, IgnoreWaterPower.class)) {
            this.setSwimming(false);
        }
    }

	// ModifyExhaustion
	@ModifyVariable(at = @At("HEAD"), method = "causeFoodExhaustion", ordinal = 0, name = "exhaustion")
	private float modifyExhaustion(float exhaustionIn) {
		return IPowerContainer.modify(this, ApoliPowers.MODIFY_EXHAUSTION.get(), exhaustionIn);
	}

	@Inject(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"))
	private void dropAdditionalInventory(CallbackInfo ci) {
		IPowerContainer.getPowers(this, ApoliPowers.INVENTORY.get()).forEach(inventory -> {
			if (inventory.getFactory().shouldDropOnDeath(inventory, this)) {
				Container container = inventory.getFactory().getInventory(inventory, this);
				for (int i = 0; i < container.getContainerSize(); ++i) {
					ItemStack itemStack = container.getItem(i);
					if (inventory.getFactory().shouldDropOnDeath(inventory, this, itemStack)) {
						if (!itemStack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemStack)) {
							container.removeItemNoUpdate(i);
						} else {
							this.drop(itemStack, true, false);
							container.setItem(i, ItemStack.EMPTY);
						}
					}
				}
			}
		});
	}
    @Inject(method = "wakeUp(ZZ)V", at = @At("HEAD"))
    private void invokeWakeUpAction(boolean bl, boolean updateSleepingPlayers, CallbackInfo ci) {
        if(!bl && !updateSleepingPlayers && getSleepingPosition().isPresent()) {
            BlockPos sleepingPos = getSleepingPosition().get();
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

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
    private void dropAdditionalInventory(CallbackInfo ci) {
        PowerHolderComponent.getPowers(this, InventoryPower.class).forEach(inventory -> {
            if(inventory.shouldDropOnDeath()) {
                for(int i = 0; i < inventory.size(); ++i) {
                    ItemStack itemStack = inventory.getStack(i);
                    if(inventory.shouldDropOnDeath(itemStack)) {
                        if (!itemStack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemStack)) {
                            inventory.removeStack(i);
                        } else {
                            ((PlayerEntity)(Object)this).dropItem(itemStack, true, false);
                            inventory.setStack(i, ItemStack.EMPTY);
                        }
                    }
                }
            }
        });
        PowerHolderComponent.getPowers(this, KeepInventoryPower.class).forEach(keepInventoryPower -> {
            keepInventoryPower.preventItemsFromDropping(inventory);
        });
    }

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V", shift = At.Shift.AFTER))
    private void restoreKeptInventory(CallbackInfo ci) {
        PowerHolderComponent.getPowers(this, KeepInventoryPower.class).forEach(keepInventoryPower -> {
            keepInventoryPower.restoreSavedItems(inventory);
        });
    }

	@Inject(method = "canTakeItem", at = @At("HEAD"), cancellable = true)
	private void preventArmorDispensing(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		EquipmentSlot slot = Mob.getEquipmentSlotForItem(stack);
		if (CoreUtils.isItemForbidden(this, slot, stack))
			info.setReturnValue(false);
	}
    @Inject(method = "canEquip", at = @At("HEAD"), cancellable = true)
    private void preventArmorDispensing(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        EquipmentSlot slot = MobEntity.getPreferredEquipmentSlot(stack);
        PowerHolderComponent component = PowerHolderComponent.KEY.get(this);
        if(component.getPowers(RestrictArmorPower.class).stream().anyMatch(rap -> !rap.canEquip(stack, slot))) {
            info.setReturnValue(false);
        }
    }
}
