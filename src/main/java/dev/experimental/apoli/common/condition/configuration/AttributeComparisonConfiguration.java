package dev.experimental.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.configuration.DoubleComparisonConfiguration;
import io.github.apace100.calio.data.SerializableDataTypes;

public record AttributeComparisonConfiguration(EntityAttribute attribute,
											   DoubleComparisonConfiguration comparison) implements IDynamicFeatureConfiguration {
	public static Codec<AttributeComparisonConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.ATTRIBUTE.fieldOf("attribute").forGetter(AttributeComparisonConfiguration::attribute),
			DoubleComparisonConfiguration.MAP_CODEC.forGetter(AttributeComparisonConfiguration::comparison)
	).apply(instance, AttributeComparisonConfiguration::new));
}
