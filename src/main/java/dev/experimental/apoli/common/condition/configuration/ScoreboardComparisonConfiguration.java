package dev.experimental.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.configuration.DoubleComparisonConfiguration;

public record ScoreboardComparisonConfiguration(DoubleComparisonConfiguration comparison,
												String objective) implements IDynamicFeatureConfiguration {
	public static Codec<ScoreboardComparisonConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			DoubleComparisonConfiguration.MAP_CODEC.forGetter(ScoreboardComparisonConfiguration::comparison),
			Codec.STRING.fieldOf("objective").forGetter(ScoreboardComparisonConfiguration::objective)
	).apply(instance, ScoreboardComparisonConfiguration::new));
}
