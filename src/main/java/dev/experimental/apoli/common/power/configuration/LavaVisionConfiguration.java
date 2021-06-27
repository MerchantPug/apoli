package dev.experimental.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;

public record LavaVisionConfiguration(float s, float v) implements IDynamicFeatureConfiguration {
	public static final Codec<LavaVisionConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.fieldOf("s").forGetter(LavaVisionConfiguration::s),
			Codec.FLOAT.fieldOf("v").forGetter(LavaVisionConfiguration::v)
	).apply(instance, LavaVisionConfiguration::new));
}
