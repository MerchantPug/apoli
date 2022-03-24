package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.FluidTagComparisonConfiguration;
import net.minecraft.world.entity.Entity;

public class FluidHeightCondition extends EntityCondition<FluidTagComparisonConfiguration> {

	public FluidHeightCondition() {
		super(FluidTagComparisonConfiguration.CODEC);
	}

	@Override
	public boolean check(FluidTagComparisonConfiguration configuration, Entity entity) {
		return configuration.comparison().check(entity.getFluidHeight(configuration.tag()));
	}
}
