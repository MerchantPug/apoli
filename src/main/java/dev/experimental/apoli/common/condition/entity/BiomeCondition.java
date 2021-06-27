package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.power.configuration.ConfiguredBiomeCondition;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.common.condition.configuration.BiomeConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.biome.Biome;

public class BiomeCondition extends EntityCondition<BiomeConfiguration> {

	public BiomeCondition() {
		super(BiomeConfiguration.CODEC);
	}

	@Override
	public boolean check(BiomeConfiguration configuration, LivingEntity entity) {
		Biome biome = entity.world.getBiome(entity.getBlockPos());
		if (!ConfiguredBiomeCondition.check(configuration.condition(), biome))
			return false;
		return entity.world.getBiomeKey(entity.getBlockPos()).map(x -> configuration.biomes().getContent().stream().anyMatch(x::equals)).orElse(false);
	}
}
