package io.github.edwinmindcraft.apoli.common.power;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.item.crafting.Recipe;

@SuppressWarnings("rawtypes")
public class RecipePower extends PowerFactory<FieldConfiguration<Recipe>> {

	public RecipePower() {
		super(FieldConfiguration.codec(SerializableDataTypes.RECIPE, "recipe"));
	}
}
