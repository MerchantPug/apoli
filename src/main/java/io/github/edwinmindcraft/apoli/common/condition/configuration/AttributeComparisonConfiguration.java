package io.github.edwinmindcraft.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.DoubleComparisonConfiguration;
import net.minecraft.world.entity.ai.attributes.Attribute;

public record AttributeComparisonConfiguration(Attribute attribute,
											   DoubleComparisonConfiguration comparison) implements IDynamicFeatureConfiguration {
	public static Codec<AttributeComparisonConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.ATTRIBUTE.fieldOf("attribute").forGetter(AttributeComparisonConfiguration::attribute),
			DoubleComparisonConfiguration.MAP_CODEC.forGetter(AttributeComparisonConfiguration::comparison)
	).apply(instance, AttributeComparisonConfiguration::new));
}
