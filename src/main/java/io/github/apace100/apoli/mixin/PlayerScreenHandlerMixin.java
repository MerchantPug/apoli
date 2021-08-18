package io.github.apace100.apoli.mixin;

import dev.experimental.apoli.common.util.CoreUtils;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/world/inventory/InventoryMenu$1")
public abstract class PlayerScreenHandlerMixin extends Slot {

	public PlayerScreenHandlerMixin(Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
	private void preventArmorInsertion(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		Player player = ((Inventory) this.container).player;
		EquipmentSlot slot = Mob.getEquipmentSlotForItem(stack);
		if (CoreUtils.isItemForbidden(player, slot, stack))
			info.setReturnValue(false);
	}
}
