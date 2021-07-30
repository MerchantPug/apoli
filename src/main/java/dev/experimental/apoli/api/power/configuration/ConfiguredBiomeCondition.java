package dev.experimental.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.ConditionData;
import dev.experimental.apoli.api.power.ConfiguredCondition;
import dev.experimental.apoli.api.power.factory.BiomeCondition;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.world.level.biome.Biome;

public final class ConfiguredBiomeCondition<C extends IDynamicFeatureConfiguration, F extends BiomeCondition<C>> extends ConfiguredCondition<C, F> {
	public static final Codec<ConfiguredBiomeCondition<?, ?>> CODEC = BiomeCondition.CODEC.dispatch(ConfiguredBiomeCondition::getFactory, Function.identity());

	public static boolean check(@Nullable ConfiguredBiomeCondition<?, ?> condition, Biome biome) {
		return condition == null || condition.check(biome);
	}

	public ConfiguredBiomeCondition(F factory, C configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	public boolean check(Biome biome) {
		return this.getFactory().check(this.getConfiguration(), this.getData(), biome);
	}
}