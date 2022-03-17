package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.access.PowerCraftingInventory;
import io.github.apace100.apoli.util.ModifiedCraftingRecipe;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyCraftingConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ResultSlot.class)
public class CraftingResultSlotMixin {

	@Shadow
	@Final
	private CraftingContainer craftSlots;

	@Inject(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getRemainingItemsFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Lnet/minecraft/core/NonNullList;"))
	private void testOnTakeItem(Player player, ItemStack stack, CallbackInfo ci) {
		if (!player.level.isClientSide()) {
			PowerCraftingInventory pci = (PowerCraftingInventory) this.craftSlots;
			ConfiguredPower<?, ?> power = pci.getPower();
			if (power != null && power.getConfiguration() instanceof ModifyCraftingConfiguration config) {
				Optional<BlockPos> blockPos = ModifiedCraftingRecipe.getBlockFromInventory(this.craftSlots);
				config.execute(player, blockPos.orElse(null));
			}
		}
	}
}
