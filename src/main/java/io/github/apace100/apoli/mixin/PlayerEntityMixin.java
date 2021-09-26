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

	@Inject(method = "canTakeItem", at = @At("HEAD"), cancellable = true)
	private void preventArmorDispensing(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		EquipmentSlot slot = Mob.getEquipmentSlotForItem(stack);
		if (CoreUtils.isItemForbidden(this, slot, stack))
			info.setReturnValue(false);
	}
}
