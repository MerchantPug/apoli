package dev.experimental.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.ConditionData;
import dev.experimental.apoli.api.power.ConfiguredCondition;
import dev.experimental.apoli.api.power.factory.BlockCondition;
import net.minecraft.block.pattern.CachedBlockPosition;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public final class ConfiguredBlockCondition<T extends IDynamicFeatureConfiguration, F extends BlockCondition<T>> extends ConfiguredCondition<T, F> {
	public static final Codec<ConfiguredBlockCondition<?, ?>> CODEC = BlockCondition.CODEC.dispatch(ConfiguredBlockCondition::getFactory, Function.identity());

	public static boolean check(@Nullable ConfiguredBlockCondition<?, ?> condition, CachedBlockPosition position) {
		return condition == null || condition.check(position);
	}

	public ConfiguredBlockCondition(F factory, T configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	public boolean check(CachedBlockPosition block) {
		return this.getFactory().check(this, block);
	}
}