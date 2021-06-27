package dev.experimental.apoli.api.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.apoli.data.ApoliDataTypes;

import java.util.Optional;

public record IntegerComparisonConfiguration(Comparison comparison,
											 int compareTo) implements IDynamicFeatureConfiguration {
	public static final MapCodec<IntegerComparisonConfiguration> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ApoliDataTypes.COMPARISON.fieldOf("comparison").forGetter(IntegerComparisonConfiguration::comparison),
			Codec.INT.fieldOf("compare_to").forGetter(IntegerComparisonConfiguration::compareTo)
	).apply(instance, IntegerComparisonConfiguration::new));

	public static final MapCodec<Optional<IntegerComparisonConfiguration>> OPTIONAL_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ApoliDataTypes.COMPARISON.optionalFieldOf("comparison").forGetter(x -> x.map(IntegerComparisonConfiguration::comparison)),
			Codec.INT.optionalFieldOf("compare_to").forGetter(x -> x.map(IntegerComparisonConfiguration::compareTo))
	).apply(instance, (t1, t2) -> t1.flatMap(x1 -> t2.map(x2 -> new IntegerComparisonConfiguration(x1, x2)))));

	public static final Codec<IntegerComparisonConfiguration> CODEC = MAP_CODEC.codec();

	public boolean check(int value) {
		return this.comparison().compare(value, this.compareTo());
	}

	public int getOptimalStoppingPoint() {
		return this.comparison().getOptimalStoppingIndex(this.compareTo());
	}
}
