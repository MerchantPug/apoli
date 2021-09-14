package io.github.edwinmindcraft.apoli.common.condition.biome;

import io.github.edwinmindcraft.apoli.api.configuration.FloatComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BiomeCondition;
import java.util.function.Function;
import net.minecraft.world.level.biome.Biome;

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
