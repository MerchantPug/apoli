package dev.experimental.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.ConditionData;
import dev.experimental.apoli.api.power.ConfiguredCondition;
import dev.experimental.apoli.api.power.factory.FluidCondition;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.world.level.material.FluidState;

public final class ConfiguredFluidCondition<C extends IDynamicFeatureConfiguration, F extends FluidCondition<C>> extends ConfiguredCondition<C, F> {
	public static final Codec<ConfiguredFluidCondition<?, ?>> CODEC = FluidCondition.CODEC.dispatch(ConfiguredFluidCondition::getFactory, Function.identity());

	public static boolean check(@Nullable ConfiguredFluidCondition<?, ?> condition, FluidState position) {
		return condition == null || condition.check(position);
	}

	public ConfiguredFluidCondition(F factory, C configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	public boolean check(FluidState fluid) {
		return this.getFactory().check(this.getConfiguration(), this.getData(), fluid);
	}
}