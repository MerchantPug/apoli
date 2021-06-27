package dev.experimental.apoli.api.configuration;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;

public record NoConfiguration() implements IDynamicFeatureConfiguration {
	public static final Codec<NoConfiguration> CODEC = Codec.unit(new NoConfiguration());
}
