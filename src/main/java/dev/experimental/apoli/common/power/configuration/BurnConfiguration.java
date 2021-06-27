package dev.experimental.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;

public record BurnConfiguration(int interval, int duration) implements IDynamicFeatureConfiguration {
	public static final Codec<BurnConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("interval").forGetter(BurnConfiguration::interval),
			Codec.INT.fieldOf("burn_duration").forGetter(BurnConfiguration::duration)
	).apply(instance, BurnConfiguration::new));
}
