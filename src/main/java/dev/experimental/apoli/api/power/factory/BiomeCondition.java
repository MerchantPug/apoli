package dev.experimental.apoli.api.power.factory;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.ConditionData;
import dev.experimental.apoli.api.power.IConditionFactory;
import dev.experimental.apoli.api.power.configuration.ConfiguredBiomeCondition;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class BiomeCondition<T extends IDynamicFeatureConfiguration> extends ForgeRegistryEntry<BiomeCondition<?>> implements IConditionFactory<T, ConfiguredBiomeCondition<T, ?>, BiomeCondition<T>> {
	public static final Codec<BiomeCondition<?>> CODEC = ApoliRegistries.codec(ApoliRegistries.BIOME_CONDITION);

	private final Codec<Pair<T, ConditionData>> codec;

	protected BiomeCondition(Codec<T> codec) {
		this.codec = IConditionFactory.conditionCodec(codec);
	}

	@Override
	public Codec<Pair<T, ConditionData>> getConditionCodec() {
		return this.codec;
	}

	@Override
	public ConfiguredBiomeCondition<T, ?> configure(T input, ConditionData configuration) {
		return new ConfiguredBiomeCondition<>(this, input, configuration);
	}

	protected boolean check(T configuration, Biome biome) {
		return false;
	}

	public boolean check(T configuration, ConditionData data, Biome biome) {
		return data.inverted() ^ this.check(configuration, biome);
	}
}
