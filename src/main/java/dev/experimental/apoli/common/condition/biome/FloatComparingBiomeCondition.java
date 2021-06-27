package dev.experimental.apoli.common.condition.biome;

import dev.experimental.apoli.api.configuration.FloatComparisonConfiguration;
import dev.experimental.apoli.api.power.factory.BiomeCondition;
import net.minecraft.world.biome.Biome;

import java.util.function.Function;

public class FloatComparingBiomeCondition extends BiomeCondition<FloatComparisonConfiguration> {
	private final Function<Biome, Float> function;

	public FloatComparingBiomeCondition(Function<Biome, Float> function) {
		super(FloatComparisonConfiguration.CODEC);
		this.function = function;
	}

	@Override
	protected boolean check(FloatComparisonConfiguration configuration, Biome biome) {
		return configuration.check(this.function.apply(biome));
	}
}
