package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.FluidCondition;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.world.level.material.FluidState;

public final class ConfiguredFluidCondition<C extends IDynamicFeatureConfiguration, F extends FluidCondition<C>> extends ConfiguredCondition<C, F> {
	public static final Codec<ConfiguredFluidCondition<?, ?>> CODEC = FluidCondition.CODEC.dispatch(ConfiguredFluidCondition::getFactory, FluidCondition::getConditionCodec);

	public static boolean check(@Nullable ConfiguredFluidCondition<?, ?> condition, FluidState position) {
		return condition == null || condition.check(position);
	}

	public ConfiguredFluidCondition(F factory, C configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	public boolean check(FluidState fluid) {
		return this.getFactory().check(this.getConfiguration(), this.getData(), fluid);
	}

	@Override
	public String toString() {
		return "CFC:" + this.getFactory().getRegistryName() + "(" + this.getData() + ")-" + this.getConfiguration();
	}
}