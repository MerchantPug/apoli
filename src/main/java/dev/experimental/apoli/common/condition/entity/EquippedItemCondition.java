package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.common.condition.configuration.EquippedItemConfiguration;
import net.minecraft.entity.LivingEntity;

public class EquippedItemCondition extends EntityCondition<EquippedItemConfiguration> {
	public EquippedItemCondition() {
		super(EquippedItemConfiguration.CODEC);
	}

	@Override
	public boolean check(EquippedItemConfiguration configuration, LivingEntity entity) {
		return configuration.condition().check(entity.getEquippedStack(configuration.slot()));
	}
}
