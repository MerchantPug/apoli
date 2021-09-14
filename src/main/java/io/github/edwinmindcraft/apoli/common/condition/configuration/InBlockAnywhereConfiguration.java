package io.github.edwinmindcraft.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;

public record InBlockAnywhereConfiguration(
		ConfiguredBlockCondition<?, ?> blockCondition,
		IntegerComparisonConfiguration comparison) implements IDynamicFeatureConfiguration {

	public static final Codec<InBlockAnywhereConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredBlockCondition.CODEC.fieldOf("block_condition").forGetter(x -> x.blockCondition),
			IntegerComparisonConfiguration.MAP_CODEC.forGetter(x -> x.comparison)
	).apply(instance, InBlockAnywhereConfiguration::new));
}
