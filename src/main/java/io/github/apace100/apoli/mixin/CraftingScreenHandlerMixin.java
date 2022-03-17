package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.access.PowerCraftingInventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingMenu.class)
public class CraftingScreenHandlerMixin {

	@Shadow
	@Final
	public ContainerLevelAccess access;

	@Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"))
	private static void clearPowerCraftingInventory(AbstractContainerMenu menu, Level level, Player player, CraftingContainer container, ResultContainer result, CallbackInfo ci) {
		((PowerCraftingInventory) container).setPower(null);
	}

	@Inject(method = "stillValid", at = @At("HEAD"), cancellable = true)
	private void allowUsingViaPower(Player player, CallbackInfoReturnable<Boolean> cir) {
		if (this.access.evaluate((world, pos) -> pos.equals(player.blockPosition()), false)) {
			cir.setReturnValue(true);
		}
	}
}
