package dev.experimental.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.configuration.IntegerComparisonConfiguration;
import dev.experimental.apoli.api.configuration.PowerReference;

public record ResourceComparisonConfiguration(IntegerComparisonConfiguration comparison,
											  PowerReference resource) implements IDynamicFeatureConfiguration {
	public static Codec<ResourceComparisonConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			IntegerComparisonConfiguration.MAP_CODEC.forGetter(ResourceComparisonConfiguration::comparison),
			PowerReference.mapCodec("resource").forGetter(ResourceComparisonConfiguration::resource)
	).apply(instance, ResourceComparisonConfiguration::new));
}
