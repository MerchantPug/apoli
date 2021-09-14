package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiomeCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.BiomeConfiguration;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;

public class BiomeCondition extends EntityCondition<BiomeConfiguration> {

	public BiomeCondition() {
		super(BiomeConfiguration.CODEC);
	}

	@Override
	public boolean check(BiomeConfiguration configuration, LivingEntity entity) {
		Biome biome = entity.level.getBiome(entity.blockPosition());
		if (!ConfiguredBiomeCondition.check(configuration.condition(), biome))
			return false;
		return entity.level.getBiomeName(entity.blockPosition()).map(x -> configuration.biomes().getContent().stream().anyMatch(x::equals)).orElse(false);
	}
}
