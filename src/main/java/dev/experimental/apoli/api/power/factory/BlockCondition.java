package dev.experimental.apoli.api.power.factory;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.ConditionData;
import dev.experimental.apoli.api.power.IConditionFactory;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockCondition;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class BlockCondition<T extends IDynamicFeatureConfiguration> extends ForgeRegistryEntry<BlockCondition<?>> implements IConditionFactory<T, ConfiguredBlockCondition<T, ?>, BlockCondition<T>> {
	public static final Codec<BlockCondition<?>> CODEC = ApoliRegistries.codec(ApoliRegistries.BLOCK_CONDITION);

	private final Codec<Pair<T, ConditionData>> codec;

	protected BlockCondition(Codec<T> codec) {
		this.codec = IConditionFactory.conditionCodec(codec);
	}

	@Override
	public Codec<Pair<T, ConditionData>> getConditionCodec() {
		return this.codec;
	}

	@Override
	public final ConfiguredBlockCondition<T, ?> configure(T input, ConditionData data) {
		return new ConfiguredBlockCondition<>(this, input, data);
	}

	protected boolean check(T configuration, BlockInWorld block) {
		return false;
	}

	public boolean check(ConfiguredBlockCondition<T, ?> configuration, BlockInWorld block) {
		return configuration.getData().inverted() ^ this.check(configuration.getConfiguration(), block);
	}
}
