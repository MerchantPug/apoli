package io.github.edwinmindcraft.apoli.common.condition.fluid;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.TagConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.FluidCondition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class InTagFluidCondition extends FluidCondition<TagConfiguration<Fluid>> {

	public InTagFluidCondition() {
		super(TagConfiguration.codec(SerializableDataTypes.FLUID_TAG, "tag"));
	}

	@Override
	public boolean check(TagConfiguration<Fluid> configuration, FluidState fluid) {
		return configuration.contains(fluid.getType());
	}
}
