package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.configuration.PowerReference;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import net.minecraft.entity.LivingEntity;

public class PowerCondition extends EntityCondition<PowerReference> {

	public PowerCondition() {super(PowerReference.codec("power"));}

	@Override
	public boolean check(PowerReference configuration, LivingEntity entity) {
		return ApoliAPI.getPowerContainer(entity).hasPower(configuration.power());
	}
}
