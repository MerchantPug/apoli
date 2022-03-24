package io.github.edwinmindcraft.apoli.common.condition.fluid;

import io.github.edwinmindcraft.apoli.api.configuration.NoConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.FluidCondition;
import net.minecraft.world.level.material.FluidState;

import java.util.function.Predicate;

public class SimpleFluidCondition extends FluidCondition<NoConfiguration> {
	private final Predicate<FluidState> predicate;

	public SimpleFluidCondition(Predicate<FluidState> predicate) {
		super(NoConfiguration.CODEC);
		this.predicate = predicate;
	}

	@Override
	public boolean check(NoConfiguration configuration, FluidState fluid) {
		return this.predicate.test(fluid);
	}
}
