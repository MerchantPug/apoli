package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;

public record ExhaustOverTimeConfiguration(int interval, float exhaustion) implements IDynamicFeatureConfiguration {
	public static final Codec<ExhaustOverTimeConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("interval").forGetter(ExhaustOverTimeConfiguration::interval),
			Codec.FLOAT.fieldOf("exhaustion").forGetter(ExhaustOverTimeConfiguration::exhaustion)
	).apply(instance, ExhaustOverTimeConfiguration::new));
}
