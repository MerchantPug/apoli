package io.github.edwinmindcraft.apoli.api.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;

public record NoConfiguration() implements IDynamicFeatureConfiguration {
	public static final Codec<NoConfiguration> CODEC = Codec.unit(new NoConfiguration());
}
