package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

public record FoodConfiguration(int food, float saturation) implements IDynamicFeatureConfiguration {

	public static Codec<FoodConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.INT.fieldOf("food").forGetter(FoodConfiguration::food),
			CalioCodecHelper.FLOAT.fieldOf("saturation").forGetter(FoodConfiguration::saturation)
	).apply(instance, FoodConfiguration::new));
}
