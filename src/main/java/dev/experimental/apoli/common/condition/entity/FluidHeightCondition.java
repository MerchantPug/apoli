package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.common.condition.configuration.FluidTagComparisonConfiguration;
import io.github.apace100.apoli.access.SubmergableEntity;
import net.minecraft.world.entity.LivingEntity;

public class FluidHeightCondition extends EntityCondition<FluidTagComparisonConfiguration> {

	public FluidHeightCondition() {
		super(FluidTagComparisonConfiguration.CODEC);
	}

	@Override
	public boolean check(FluidTagComparisonConfiguration configuration, LivingEntity entity) {
		return configuration.comparison().check(((SubmergableEntity) entity).getFluidHeightLoosely(configuration.tag()));
	}
}
