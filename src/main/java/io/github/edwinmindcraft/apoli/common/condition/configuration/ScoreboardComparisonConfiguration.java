package io.github.edwinmindcraft.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.DoubleComparisonConfiguration;

public record ScoreboardComparisonConfiguration(DoubleComparisonConfiguration comparison,
												String objective) implements IDynamicFeatureConfiguration {
	public static Codec<ScoreboardComparisonConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			DoubleComparisonConfiguration.MAP_CODEC.forGetter(ScoreboardComparisonConfiguration::comparison),
			Codec.STRING.fieldOf("objective").forGetter(ScoreboardComparisonConfiguration::objective)
	).apply(instance, ScoreboardComparisonConfiguration::new));
}
