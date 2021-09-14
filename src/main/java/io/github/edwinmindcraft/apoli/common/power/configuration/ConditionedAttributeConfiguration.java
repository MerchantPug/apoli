package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;

public record ConditionedAttributeConfiguration(ListConfiguration<AttributedEntityAttributeModifier> modifiers,
												int tickRate) implements IDynamicFeatureConfiguration {
	public static final Codec<ConditionedAttributeConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.mapCodec(ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIER, "modifier", "modifiers").forGetter(ConditionedAttributeConfiguration::modifiers),
			Codec.INT.optionalFieldOf("tick_rate", 20).forGetter(ConditionedAttributeConfiguration::tickRate)
	).apply(instance, ConditionedAttributeConfiguration::new));
}
