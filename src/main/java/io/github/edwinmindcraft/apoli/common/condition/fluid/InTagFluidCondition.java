package io.github.edwinmindcraft.apoli.common.condition.fluid;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.FluidCondition;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class InTagFluidCondition extends FluidCondition<FieldConfiguration<Tag<Fluid>>> {

	public InTagFluidCondition() {
		super(FieldConfiguration.codec(SerializableDataTypes.FLUID_TAG, "tag"));
	}

	@Override
	public boolean check(FieldConfiguration<Tag<Fluid>> configuration, FluidState fluid) {
		return fluid.is(configuration.value());
	}
}
