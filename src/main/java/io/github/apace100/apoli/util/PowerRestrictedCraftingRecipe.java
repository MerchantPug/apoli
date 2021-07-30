package io.github.apace100.apoli.util;

import com.google.common.collect.Lists;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.mixin.CraftingInventoryAccessor;
import io.github.apace100.apoli.mixin.CraftingScreenHandlerAccessor;
import io.github.apace100.apoli.mixin.PlayerScreenHandlerAccessor;
import io.github.apace100.apoli.power.RecipePower;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

public class PowerRestrictedCraftingRecipe extends CustomRecipe {

    public static final RecipeSerializer<?> SERIALIZER = new SimpleRecipeSerializer<>(PowerRestrictedCraftingRecipe::new);

    public PowerRestrictedCraftingRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level world) {
        return getRecipes(inv).stream().anyMatch(r -> r.matches(inv, world));
    }

    @Override
    public ItemStack craft(CraftingContainer inv) {
        Player player = getPlayerFromInventory(inv);
        if(player != null) {
            Optional<Recipe<CraftingContainer>> optional = getRecipes(inv).stream().filter(r -> r.matches(inv, player.level)).findFirst();
            if(optional.isPresent()) {
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
        AbstractContainerMenu handler = ((CraftingInventoryAccessor)inv).getHandler();
        return getPlayerFromHandler(handler);
    }

    private List<Recipe<CraftingContainer>> getRecipes(CraftingContainer inv) {
        AbstractContainerMenu handler = ((CraftingInventoryAccessor)inv).getHandler();
        Player player = getPlayerFromHandler(handler);
        if(player != null) {
            return PowerHolderComponent.getPowers(player, RecipePower.class).stream().map(RecipePower::getRecipe).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    private Player getPlayerFromHandler(AbstractContainerMenu screenHandler) {
        if(screenHandler instanceof CraftingMenu) {
            return ((CraftingScreenHandlerAccessor)screenHandler).getPlayer();
        }
        if(screenHandler instanceof InventoryMenu) {
            return ((PlayerScreenHandlerAccessor)screenHandler).getOwner();
        }
        return null;
    }
}
