package io.github.apace100.apoli.power;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;

public class RecipePower extends Power {

    private final Recipe<CraftingContainer> recipe;

    public RecipePower(PowerType<?> type, LivingEntity entity, Recipe<CraftingContainer> recipe) {
        super(type, entity);
        this.recipe = recipe;
    }

    public Recipe<CraftingContainer> getRecipe() {
        return recipe;
    }
}
