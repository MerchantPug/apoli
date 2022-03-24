package io.github.edwinmindcraft.apoli.common.condition.biome;

import io.github.edwinmindcraft.apoli.api.configuration.FloatComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BiomeCondition;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Function;

public class FloatComparingBiomeCondition extends BiomeCondition<FloatComparisonConfiguration> {
	private final Function<Holder<Biome>, Float> function;

	public FloatComparingBiomeCondition(Function<Holder<Biome>, Float> function) {
		super(FloatComparisonConfiguration.CODEC);
		this.function = function;
	}

	@Override
	protected boolean check(FloatComparisonConfiguration configuration, Holder<Biome> biome) {
		return configuration.check(this.function.apply(biome));
	}
}
