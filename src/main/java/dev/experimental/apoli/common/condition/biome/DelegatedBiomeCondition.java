package dev.experimental.apoli.common.condition.biome;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.power.factory.BiomeCondition;
import dev.experimental.apoli.common.condition.meta.IDelegatedConditionConfiguration;
import net.minecraft.world.level.biome.Biome;

public class DelegatedBiomeCondition<T extends IDelegatedConditionConfiguration<Biome>> extends BiomeCondition<T> {
	public DelegatedBiomeCondition(Codec<T> codec) {
		super(codec);
	}

	@Override
	protected boolean check(T configuration, Biome biome) {
		return configuration.check(biome);
	}
}
