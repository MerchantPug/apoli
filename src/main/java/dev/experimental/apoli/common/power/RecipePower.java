package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.world.item.crafting.Recipe;

public class RecipePower extends PowerFactory<FieldConfiguration<Recipe>> {

	public RecipePower() {
		super(FieldConfiguration.codec(SerializableDataTypes.RECIPE, "recipe"));
	}
}
