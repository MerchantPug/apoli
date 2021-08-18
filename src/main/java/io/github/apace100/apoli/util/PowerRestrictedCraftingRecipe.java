package io.github.apace100.apoli.util;

import com.google.common.collect.Lists;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.common.registry.ModPowers;
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
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PowerRestrictedCraftingRecipe extends CustomRecipe {

	public static final RecipeSerializer<?> SERIALIZER = new SimpleRecipeSerializer<>(PowerRestrictedCraftingRecipe::new);

	public PowerRestrictedCraftingRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer inv, Level world) {
		return this.getRecipes(inv).stream().anyMatch(r -> r.matches(inv, world));
	}

	@Override
	public ItemStack assemble(CraftingContainer inv) {
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
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private Player getPlayerFromInventory(CraftingContainer inv) {
		return this.getPlayerFromHandler(inv.menu);
	}

	@SuppressWarnings("unchecked")
	private List<Recipe<CraftingContainer>> getRecipes(CraftingContainer inv) {
		Player player = this.getPlayerFromHandler(inv.menu);
		if (player != null) {
			return IPowerContainer.getPowers(player, ModPowers.RECIPE.get()).stream().map(x -> (Recipe<CraftingContainer>) x.getConfiguration().value()).toList();
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
