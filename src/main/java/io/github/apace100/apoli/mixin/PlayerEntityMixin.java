package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ActionOnBeingUsedPower;
import io.github.apace100.apoli.power.ActionOnEntityUsePower;
import io.github.apace100.apoli.power.KeepInventoryPower;
import io.github.apace100.apoli.power.PreventBeingUsedPower;
import io.github.apace100.apoli.power.PreventEntityUsePower;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import io.github.edwinmindcraft.apoli.common.util.CoreUtils;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.Container;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

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

	@Shadow public abstract Inventory getInventory();

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

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"), index = 1)
    private double adjustVerticalSwimSpeed(double original) {
        return PowerHolderComponent.modify(this, ModifySwimSpeedPower.class, original);
    }

    @Inject(method = "dismountVehicle", at = @At("HEAD"))
    private void sendPlayerDismountPacket(CallbackInfo ci) {
        if(!world.isClient && getVehicle() instanceof PlayerEntity) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(getId());
            ServerPlayNetworking.send((ServerPlayerEntity) getVehicle(), ModPackets.PLAYER_DISMOUNT, buf);
        }
    }

	// ModifyExhaustion
	@ModifyVariable(at = @At("HEAD"), method = "causeFoodExhaustion", ordinal = 0, name = "exhaustion")
	private float modifyExhaustion(float exhaustionIn) {
		return IPowerContainer.modify(this, ApoliPowers.MODIFY_EXHAUSTION.get(), exhaustionIn);
	}

    // Prevent healing if DisableRegenPower
    // Note that this function was called "shouldHeal" instead of "canFoodHeal" at some point in time.
    /*@Inject(method = "canFoodHeal", at = @At("HEAD"), cancellable = true)
    private void disableHeal(CallbackInfoReturnable<Boolean> info) {
        if(PowerHolderComponent.hasPower(this, DisableRegenPower.class)) {
            info.setReturnValue(false);
        }
    }*/

	@Inject(method = "canTakeItem", at = @At("HEAD"), cancellable = true)
	private void preventArmorDispensing(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		EquipmentSlot slot = Mob.getEquipmentSlotForItem(stack);
		if (CoreUtils.isItemForbidden(this, slot, stack))
			info.setReturnValue(false);
	}
}
