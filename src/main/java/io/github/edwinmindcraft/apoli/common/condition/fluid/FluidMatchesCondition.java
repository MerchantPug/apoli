package io.github.edwinmindcraft.apoli.common.condition.fluid;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.FluidCondition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class FluidMatchesCondition extends FluidCondition<FieldConfiguration<Fluid>> {
	public FluidMatchesCondition() {
		super(FieldConfiguration.codec(SerializableDataTypes.FLUID, "fluid"));
	}

	@Override
	public boolean check(FieldConfiguration<Fluid> configuration, FluidState fluid) {
		return fluid.is(configuration.value());
	}
}
