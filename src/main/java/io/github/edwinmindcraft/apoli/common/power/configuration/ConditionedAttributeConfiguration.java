package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

public record ConditionedAttributeConfiguration(ListConfiguration<AttributedEntityAttributeModifier> modifiers,
												int tickRate) implements IDynamicFeatureConfiguration {
	public static final Codec<ConditionedAttributeConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.mapCodec(ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIER, "modifier", "modifiers").forGetter(ConditionedAttributeConfiguration::modifiers),
			CalioCodecHelper.optionalField(Codec.INT, "tick_rate", 20).forGetter(ConditionedAttributeConfiguration::tickRate)
	).apply(instance, ConditionedAttributeConfiguration::new));
}
