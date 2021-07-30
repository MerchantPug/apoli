package dev.experimental.apoli.common.condition.biome;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.configuration.NoConfiguration;
import dev.experimental.apoli.api.power.factory.BiomeCondition;
import net.minecraft.world.level.biome.Biome;

public class HighHumidityCondition extends BiomeCondition<NoConfiguration> {

	public static final Codec<HighHumidityCondition> CODEC = Codec.unit(new HighHumidityCondition());

	public HighHumidityCondition() {
		super(NoConfiguration.CODEC);
	}

	@Override
	protected boolean check(NoConfiguration configuration, Biome biome) {
		return biome.isHumid();
	}
}
