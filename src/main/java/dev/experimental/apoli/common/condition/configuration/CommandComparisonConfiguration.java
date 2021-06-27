package dev.experimental.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.common.action.configuration.CommandConfiguration;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.configuration.IntegerComparisonConfiguration;

public record CommandComparisonConfiguration(CommandConfiguration command,
											 IntegerComparisonConfiguration comparison) implements IDynamicFeatureConfiguration {
	public static final Codec<CommandComparisonConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CommandConfiguration.MAP_CODEC.forGetter(CommandComparisonConfiguration::command),
			IntegerComparisonConfiguration.MAP_CODEC.forGetter(CommandComparisonConfiguration::comparison)
	).apply(instance, CommandComparisonConfiguration::new));
}
