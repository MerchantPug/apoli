package io.github.edwinmindcraft.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.common.action.configuration.CommandConfiguration;

public record CommandComparisonConfiguration(CommandConfiguration command,
											 IntegerComparisonConfiguration comparison) implements IDynamicFeatureConfiguration {
	public static final Codec<CommandComparisonConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CommandConfiguration.MAP_CODEC.forGetter(CommandComparisonConfiguration::command),
			IntegerComparisonConfiguration.MAP_CODEC.forGetter(CommandComparisonConfiguration::comparison)
	).apply(instance, CommandComparisonConfiguration::new));
}
