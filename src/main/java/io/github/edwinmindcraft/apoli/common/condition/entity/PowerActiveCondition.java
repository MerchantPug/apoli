package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.PowerReference;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.world.entity.Entity;

public class PowerActiveCondition extends EntityCondition<PowerReference> {

	public PowerActiveCondition() {
		super(PowerReference.codec("power"));
	}

	@Override
	public boolean check(PowerReference configuration, Entity entity) {
		return IPowerContainer.get(entity).filter(x -> x.hasPower(configuration.power()))
				.map(x -> x.getPower(configuration.power()))
				.map(x -> x.isActive(entity)).orElse(false);
	}
}
