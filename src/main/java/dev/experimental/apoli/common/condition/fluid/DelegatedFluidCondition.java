package dev.experimental.apoli.common.condition.fluid;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.power.factory.FluidCondition;
import dev.experimental.apoli.common.condition.meta.IDelegatedConditionConfiguration;
import net.minecraft.fluid.FluidState;

public class DelegatedFluidCondition<T extends IDelegatedConditionConfiguration<FluidState>> extends FluidCondition<T> {
	public DelegatedFluidCondition(Codec<T> codec) {
		super(codec);
	}


	@Override
	public boolean check(T configuration, FluidState fluid) {
		return configuration.check(fluid);
	}
}
