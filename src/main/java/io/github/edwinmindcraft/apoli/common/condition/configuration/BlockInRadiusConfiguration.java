package io.github.edwinmindcraft.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.apace100.apoli.util.Shape;
import io.github.apace100.calio.data.SerializableDataType;

public record BlockInRadiusConfiguration(
		ConfiguredBlockCondition<?, ?> blockCondition, int radius,
		Shape shape, IntegerComparisonConfiguration comparison) implements IDynamicFeatureConfiguration {

	public static final Codec<BlockInRadiusConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredBlockCondition.CODEC.fieldOf("block_condition").forGetter(x -> x.blockCondition),
			Codec.INT.fieldOf("radius").forGetter(x -> x.radius),
			SerializableDataType.enumValue(Shape.class).optionalFieldOf("shape", Shape.CUBE).forGetter(x -> x.shape),
			IntegerComparisonConfiguration.MAP_CODEC.forGetter(x -> x.comparison)
	).apply(instance, BlockInRadiusConfiguration::new));
}
