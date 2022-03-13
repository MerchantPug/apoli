package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.EquippedItemConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EquippedItemCondition extends EntityCondition<EquippedItemConfiguration> {
	public EquippedItemCondition() {
		super(EquippedItemConfiguration.CODEC);
	}

	@Override
	public boolean check(EquippedItemConfiguration configuration, Entity entity) {
		return entity instanceof LivingEntity living && configuration.condition().check(living.level, living.getItemBySlot(configuration.slot()));
	}
}
