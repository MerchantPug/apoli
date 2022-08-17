package io.github.apace100.apoli.util;

import com.google.common.collect.Lists;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import io.github.edwinmindcraft.apoli.common.registry.ApoliRecipeSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class PowerRestrictedCraftingRecipe extends CustomRecipe {

	public PowerRestrictedCraftingRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@NotNull CraftingContainer inv, @NotNull Level world) {
		return this.getRecipes(inv).stream().anyMatch(r -> r.matches(inv, world));
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull CraftingContainer inv) {
		Player player = this.getPlayerFromInventory(inv);
		if (player != null) {
			Optional<Recipe<CraftingContainer>> optional = this.getRecipes(inv).stream().filter(r -> r.matches(inv, player.level)).findFirst();
			if (optional.isPresent()) {
				Recipe<CraftingContainer> recipe = optional.get();
				return recipe.assemble(inv);
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
		return ApoliRecipeSerializers.POWER_RESTRICTED.get();
	}

	private Player getPlayerFromInventory(CraftingContainer inv) {
		return this.getPlayerFromHandler(inv.menu);
	}

	@SuppressWarnings("unchecked")
	private List<Recipe<CraftingContainer>> getRecipes(CraftingContainer inv) {
		Player player = this.getPlayerFromHandler(inv.menu);
		if (player != null) {
			return IPowerContainer.getPowers(player, ApoliPowers.RECIPE.get()).stream().map(x -> (Recipe<CraftingContainer>) x.value().getConfiguration().value()).toList();
		}
		return Lists.newArrayList();
	}

	private Player getPlayerFromHandler(AbstractContainerMenu screenHandler) {
		if (screenHandler instanceof CraftingMenu menu) {
			return menu.player;
		}
		if (screenHandler instanceof InventoryMenu menu) {
			return menu.owner;
		}
		return null;
	}
}
