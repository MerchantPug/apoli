package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.AttributeComparisonConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class AttributeCondition extends EntityCondition<AttributeComparisonConfiguration> {

	public AttributeCondition() {
		super(AttributeComparisonConfiguration.CODEC);
	}

	@Override
	public boolean check(AttributeComparisonConfiguration configuration, Entity entity) {
		double value = 0;
		if (entity instanceof LivingEntity) {
			AttributeInstance instance = ((LivingEntity) entity).getAttribute(configuration.attribute());
			value = instance != null ? instance.getValue() : value;
		}
		return configuration.comparison().check(value);
	}
}

