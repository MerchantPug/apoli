package io.github.apace100.apoli.util;

import com.google.common.collect.Lists;
import io.github.apace100.apoli.access.PowerCraftingInventory;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyCraftingPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyCraftingConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import io.github.edwinmindcraft.apoli.common.registry.ApoliRecipeSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModifiedCraftingRecipe extends CustomRecipe {

	public ModifiedCraftingRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@NotNull CraftingContainer inv, @NotNull Level world) {
		Player player = getCraftingPlayer(inv);
		List<ConfiguredPower<ModifyCraftingConfiguration, ModifyCraftingPower>> recipes = this.getRecipes(player);
		if (recipes.isEmpty())
			return false;
		Optional<CraftingRecipe> original = this.getOriginalMatch(inv, player);
		return original.isPresent() && recipes.stream().anyMatch(r -> r.getConfiguration().doesApply(inv, original.get(), world));
	}

	private static Player getCraftingPlayer(@NotNull CraftingContainer inv) {
		Player player = ForgeHooks.getCraftingPlayer();
		if (player != null)
			return player;
		return getPlayerFromInventory(inv);
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull CraftingContainer inv) {
		Player player = getCraftingPlayer(inv);
		if (player != null) {
			Optional<CraftingRecipe> original = this.getOriginalMatch(inv, player);
			if (original.isPresent()) {
				Optional<ConfiguredPower<ModifyCraftingConfiguration, ModifyCraftingPower>> optional = this.getRecipes(player).stream().filter(r -> r.getConfiguration().doesApply(inv, original.get(), player.level)).findFirst();
				if (optional.isPresent()) {
					ItemStack result = optional.get().getConfiguration().createResult(inv, original.get(), player.level);
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
		return ApoliRecipeSerializers.MODIFIED.get();
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

	private List<ConfiguredPower<ModifyCraftingConfiguration, ModifyCraftingPower>> getRecipes(Player player) {
		if (player != null)
			return IPowerContainer.getPowers(player, ApoliPowers.MODIFY_CRAFTING.get()).stream().map(Holder::value).collect(Collectors.toList());
		return Lists.newArrayList();
	}

	private Optional<CraftingRecipe> getOriginalMatch(CraftingContainer inv, Player player) {
		if (player != null && player.getServer() != null) {
			final Level level = player.level;
			return player.getServer().getRecipeManager().byType(RecipeType.CRAFTING).values().stream()
					.filter((cr) -> !(cr instanceof ModifiedCraftingRecipe) && cr.matches(inv, level))
					.min(Comparator.comparing((p_220247_) -> p_220247_.getResultItem().getDescriptionId()));
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
