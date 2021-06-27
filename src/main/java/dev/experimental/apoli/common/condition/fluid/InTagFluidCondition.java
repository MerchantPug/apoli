package dev.experimental.apoli.common.condition.fluid;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.FluidCondition;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.Tag;

public class InTagFluidCondition extends FluidCondition<FieldConfiguration<Tag<Fluid>>> {

	public InTagFluidCondition() {
		super(FieldConfiguration.codec(SerializableDataTypes.FLUID_TAG, "tag"));
	}

	@Override
	public boolean check(FieldConfiguration<Tag<Fluid>> configuration, FluidState fluid) {
		return fluid.isIn(configuration.value());
	}
}
