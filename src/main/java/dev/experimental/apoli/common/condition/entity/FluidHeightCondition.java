package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.common.condition.configuration.FluidTagComparisonConfiguration;
import net.minecraft.entity.LivingEntity;

public class FluidHeightCondition extends EntityCondition<FluidTagComparisonConfiguration> {

	public FluidHeightCondition() {
		super(FluidTagComparisonConfiguration.CODEC);
	}

	@Override
	public boolean check(FluidTagComparisonConfiguration configuration, LivingEntity entity) {
		return configuration.comparison().check(entity.getFluidHeight(configuration.tag()));
	}
}
