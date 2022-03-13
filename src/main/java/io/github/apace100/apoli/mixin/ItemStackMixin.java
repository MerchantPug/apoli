package io.github.apace100.apoli.mixin;

import io.github.edwinmindcraft.apoli.common.power.ItemOnItemPower;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	//Moved from ItemMixin to prevent overrides from other mods from interfering too much.
	@Inject(method = "overrideOtherStackedOnMe", at = @At("RETURN"), cancellable = true)
	public void forgeItem(ItemStack other, Slot slot, ClickAction pAction, Player pPlayer, SlotAccess otherAccess, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue())
			return;
		if (pAction != ClickAction.SECONDARY)
			return;
		if (ItemOnItemPower.execute(pPlayer, slot, otherAccess))
			cir.setReturnValue(true);
	}
}
