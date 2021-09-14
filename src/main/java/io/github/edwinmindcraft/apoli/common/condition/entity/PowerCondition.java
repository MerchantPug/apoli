package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.PowerReference;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.world.entity.LivingEntity;

public class PowerCondition extends EntityCondition<PowerReference> {

	public PowerCondition() {super(PowerReference.codec("power"));}

	@Override
	public boolean check(PowerReference configuration, LivingEntity entity) {
		return IPowerContainer.get(entity).map(x -> x.hasPower(configuration.power())).orElse(false);
	}
}
