package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.PowerReference;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import net.minecraft.world.entity.LivingEntity;

public class PowerActiveCondition extends EntityCondition<PowerReference> {

	public PowerActiveCondition() {
		super(PowerReference.codec("power"));
	}

	@Override
	public boolean check(PowerReference configuration, LivingEntity entity) {
		IPowerContainer component = ApoliAPI.getPowerContainer(entity);
		return entity instanceof PlayerEntity player && component.hasPower(configuration.power()) && component.getPower(configuration.power()).isActive(player);
	}
}
