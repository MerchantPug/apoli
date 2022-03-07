package io.github.edwinmindcraft.apoli.common.condition.item;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class IngredientCondition extends ItemCondition<FieldConfiguration<Ingredient>> {

	public IngredientCondition() {
		//TODO 1.18.1 Fix the ingredient system.
		super(FieldConfiguration.codec(SerializableDataTypes.VANILLA_INGREDIENT, "ingredient"));
	}

	@Override
	public boolean check(FieldConfiguration<Ingredient> configuration, ItemStack stack) {
		return configuration.value().test(stack);
	}
}
