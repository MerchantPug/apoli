package io.github.edwinmindcraft.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;

public record AdjacentConfiguration(IntegerComparisonConfiguration comparison,
									ConfiguredBlockCondition<?, ?> condition) implements IDynamicFeatureConfiguration {
	public static final Codec<AdjacentConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			IntegerComparisonConfiguration.MAP_CODEC.forGetter(AdjacentConfiguration::comparison),
			ConfiguredBlockCondition.CODEC.fieldOf("adjacent_condition").forGetter(AdjacentConfiguration::condition)
	).apply(instance, AdjacentConfiguration::new));

	@Override
	public boolean isConfigurationValid() {
		return this.condition().isConfigurationValid();
	}
}
