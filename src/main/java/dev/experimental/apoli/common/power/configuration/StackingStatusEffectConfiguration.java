package dev.experimental.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.configuration.ListConfiguration;
import io.github.apace100.calio.data.SerializableDataTypes;

public record StackingStatusEffectConfiguration(ListConfiguration<StatusEffectInstance> effects,
												int min, int max,
												int duration) implements IDynamicFeatureConfiguration {
	public static final Codec<StackingStatusEffectConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.mapCodec(SerializableDataTypes.STATUS_EFFECT_INSTANCE, "effect", "effects").forGetter(StackingStatusEffectConfiguration::effects),
			Codec.INT.fieldOf("min_stacks").forGetter(StackingStatusEffectConfiguration::min),
			Codec.INT.fieldOf("max_stacks").forGetter(StackingStatusEffectConfiguration::min),
			Codec.INT.fieldOf("duration_per_stack").forGetter(StackingStatusEffectConfiguration::duration)
	).apply(instance, StackingStatusEffectConfiguration::new));
}
