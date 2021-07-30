package dev.experimental.apoli.api.power.factory;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import dev.architectury.core.RegistryEntry;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.ConditionData;
import dev.experimental.apoli.api.power.IConditionFactory;
import dev.experimental.apoli.api.power.configuration.ConfiguredFluidCondition;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import net.minecraft.world.level.material.FluidState;

public abstract class FluidCondition<T extends IDynamicFeatureConfiguration> extends RegistryEntry<FluidCondition<?>> implements IConditionFactory<T, ConfiguredFluidCondition<T, ?>, FluidCondition<T>> {
	public static final Codec<FluidCondition<?>> CODEC = ApoliRegistries.codec(ApoliRegistries.FLUID_CONDITION);
	private final Codec<Pair<T, ConditionData>> codec;

	protected FluidCondition(Codec<T> codec) {
		this.codec = IConditionFactory.conditionCodec(codec);
	}

	@Override
	public Codec<Pair<T, ConditionData>> getConditionCodec() {
		return this.codec;
	}

	@Override
	public final ConfiguredFluidCondition<T, ?> configure(T input, ConditionData data) {
		return new ConfiguredFluidCondition<>(this, input, data);
	}

	public boolean check(T configuration, FluidState fluid) {
		return false;
	}

	public boolean check(T configuration, ConditionData data, FluidState fluid) {
		return data.inverted() ^ this.check(configuration, fluid);
	}
}
