package io.github.apace100.apoli.power.factory.condition.item;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.access.EntityLinkedItemStack;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.world.World;

import java.util.Optional;

public class SmeltableCondition {

    private static boolean condition(SerializableData.Instance data, ItemStack stack) {
        World world = ((EntityLinkedItemStack)stack).getEntity().getWorld();
        if(world == null) {
            return false;
        }
         Optional<SmeltingRecipe> optional = world.getRecipeManager()
                .getFirstMatch(
                        RecipeType.SMELTING,
                        new SimpleInventory(stack),
                        world
                );
        return optional.isPresent();
    }

    public static ConditionFactory<ItemStack> getFactory() {
        return new ConditionFactory<>(Apoli.identifier("smeltable"),
                new SerializableData()
                        .add("check_state", SerializableDataTypes.BOOLEAN, false)
                        .add("check_ability", SerializableDataTypes.BOOLEAN, true),
                SmeltableCondition::condition
        );
    }
}
