package dev.experimental.apoli.api.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.apoli.data.ApoliDataTypes;

public record DoubleComparisonConfiguration(Comparison comparison,
											double compareTo) implements IDynamicFeatureConfiguration {
	public static final MapCodec<DoubleComparisonConfiguration> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ApoliDataTypes.COMPARISON.fieldOf("comparison").forGetter(DoubleComparisonConfiguration::comparison),
			Codec.DOUBLE.fieldOf("compare_to").forGetter(DoubleComparisonConfiguration::compareTo)
	).apply(instance, DoubleComparisonConfiguration::new));

	public static final Codec<DoubleComparisonConfiguration> CODEC = MAP_CODEC.codec();

	public boolean check(double value) {
		return this.comparison().compare(value, this.compareTo());
	}
}
