package dev.experimental.apoli.common.condition.fluid;

import dev.experimental.apoli.api.configuration.NoConfiguration;
import dev.experimental.apoli.api.power.factory.FluidCondition;
import net.minecraft.fluid.FluidState;

import java.util.function.Predicate;

public class SimpleFluidCondition extends FluidCondition<NoConfiguration> {
	private final Predicate<FluidState> predicate;

	public SimpleFluidCondition(Predicate<FluidState> predicate) {
		super(NoConfiguration.CODEC);
		this.predicate = predicate;
	}

	@Override
	public boolean check(NoConfiguration configuration, FluidState fluid) {
		return predicate.test(fluid);
	}
}
