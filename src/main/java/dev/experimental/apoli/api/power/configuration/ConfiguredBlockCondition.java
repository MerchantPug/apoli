package dev.experimental.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.ConditionData;
import dev.experimental.apoli.api.power.ConfiguredCondition;
import dev.experimental.apoli.api.power.factory.BlockCondition;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public final class ConfiguredBlockCondition<T extends IDynamicFeatureConfiguration, F extends BlockCondition<T>> extends ConfiguredCondition<T, F> {
	public static final Codec<ConfiguredBlockCondition<?, ?>> CODEC = BlockCondition.CODEC.dispatch(ConfiguredBlockCondition::getFactory, Function.identity());

	public static boolean check(@Nullable ConfiguredBlockCondition<?, ?> condition, BlockInWorld position) {
		return condition == null || condition.check(position);
	}

	public ConfiguredBlockCondition(F factory, T configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	public boolean check(BlockInWorld block) {
		return this.getFactory().check(this, block);
	}
}