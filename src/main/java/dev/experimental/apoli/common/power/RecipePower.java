package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.util.codec.RecipeCodec;
import net.minecraft.recipe.Recipe;

public class RecipePower extends PowerFactory<FieldConfiguration<Recipe<?>>> {

	public RecipePower() {
		super(FieldConfiguration.codec(RecipeCodec.CODEC, "recipe"));
	}
}
