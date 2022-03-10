package io.github.apace100.apoli.util;

import com.google.common.collect.Lists;
import io.github.apace100.apoli.access.PowerCraftingInventory;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.mixin.CraftingContainerAccessor;
import io.github.apace100.apoli.power.ModifyCraftingPower;
import net.minecraft.core.BlockPos;
import net.minecraft.recipe.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ModifiedCraftingRecipe extends CustomRecipe {

	public static final RecipeSerializer<?> SERIALIZER = new SimpleRecipeSerializer<>(ModifiedCraftingRecipe::new);

	public ModifiedCraftingRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer inv, Level world) {
		Player player = ForgeHooks.getCraftingPlayer();
		if (player == null) player = getPlayerFromInventory(inv);
		Optional<CraftingRecipe> original = this.getOriginalMatch(inv, player);
		if (original.isEmpty()) {
			return false;
		}
		return this.getRecipes(player).stream().anyMatch(r -> r.doesApply(inv, original.get()));
	}

	@Override
	public @NotNull ItemStack assemble(CraftingContainer inv) {
		Player player = ForgeHooks.getCraftingPlayer();
		if (player == null) player = getPlayerFromInventory(inv);
		if (player != null) {
			Optional<CraftingRecipe> original = this.getOriginalMatch(inv, player);
			if (original.isPresent()) {
				Optional<ModifyCraftingPower> optional = this.getRecipes(player).stream().filter(r -> r.doesApply(inv, original.get())).findFirst();
				if (optional.isPresent()) {
					ItemStack result = optional.get().getNewResult(inv, original.get());
					((PowerCraftingInventory) inv).setPower(optional.get());
					return result;
				}
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static Player getPlayerFromInventory(CraftingContainer inv) {
		AbstractContainerMenu handler = inv.menu;
		return getPlayerFromHandler(handler);
	}

	public static Optional<BlockPos> getBlockFromInventory(CraftingContainer inv) {
		AbstractContainerMenu handler = inv.menu;
		if (handler instanceof CraftingMenu menu) {
			return menu.access.evaluate((world, blockPos) -> blockPos);
		}
		return Optional.empty();
	}

	private List<ModifyCraftingPower> getRecipes(Player player) {
		if (player != null)
			return PowerHolderComponent.getPowers(player, ModifyCraftingPower.class);
		return Lists.newArrayList();
	}

	private Optional<CraftingRecipe> getOriginalMatch(CraftingContainer inv, Player player) {
		if (player != null && player.getServer() != null) {
			List<CraftingRecipe> recipes = player.getServer().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
			return recipes.stream()
					.filter(cr -> !(cr instanceof ModifiedCraftingRecipe) && cr.matches(inv, player.level))
					.findFirst();
		}
		return Optional.empty();
	}

	private static Player getPlayerFromHandler(AbstractContainerMenu screenHandler) {
		if (screenHandler instanceof CraftingMenu menu)
			return menu.player;
		if (screenHandler instanceof InventoryMenu menu)
			return menu.owner;
		return null;
	}
}
