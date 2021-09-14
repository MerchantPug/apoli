package io.github.edwinmindcraft.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.PowerReference;

public record ResourceComparisonConfiguration(IntegerComparisonConfiguration comparison,
											  PowerReference resource) implements IDynamicFeatureConfiguration {
	public static Codec<ResourceComparisonConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			IntegerComparisonConfiguration.MAP_CODEC.forGetter(ResourceComparisonConfiguration::comparison),
			PowerReference.mapCodec("resource").forGetter(ResourceComparisonConfiguration::resource)
	).apply(instance, ResourceComparisonConfiguration::new));
}
