package io.github.edwinmindcraft.apoli.common.condition.biome;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.power.factory.BiomeCondition;
import io.github.edwinmindcraft.apoli.common.condition.meta.IDelegatedConditionConfiguration;
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
