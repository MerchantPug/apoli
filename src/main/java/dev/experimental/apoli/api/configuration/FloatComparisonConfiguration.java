package dev.experimental.apoli.api.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.apoli.data.ApoliDataTypes;

public record FloatComparisonConfiguration(Comparison comparison,
										   float compareTo) implements IDynamicFeatureConfiguration {
	public static final MapCodec<FloatComparisonConfiguration> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ApoliDataTypes.COMPARISON.fieldOf("comparison").forGetter(FloatComparisonConfiguration::comparison),
			Codec.FLOAT.fieldOf("compare_to").forGetter(FloatComparisonConfiguration::compareTo)
	).apply(instance, FloatComparisonConfiguration::new));

	public static final Codec<FloatComparisonConfiguration> CODEC = MAP_CODEC.codec();

	public boolean check(float value) {
		return this.comparison().compare(value, this.compareTo());
	}
}
