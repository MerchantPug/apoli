package dev.experimental.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.configuration.ListConfiguration;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;

public record AttributeConfiguration(ListConfiguration<AttributedEntityAttributeModifier> modifiers, boolean updateHealth) implements IDynamicFeatureConfiguration {
	public static final Codec<AttributeConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.mapCodec(ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIER, "modifier", "modifiers").forGetter(AttributeConfiguration::modifiers),
			Codec.BOOL.optionalFieldOf("update_health", true).forGetter(AttributeConfiguration::updateHealth)
	).apply(instance, AttributeConfiguration::new));
}
