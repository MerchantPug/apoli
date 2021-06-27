package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.common.condition.configuration.AttributeComparisonConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;

public class AttributeCondition extends EntityCondition<AttributeComparisonConfiguration> {

	public AttributeCondition() {
		super(AttributeComparisonConfiguration.CODEC);
	}

	@Override
	public boolean check(AttributeComparisonConfiguration configuration, LivingEntity entity) {
		EntityAttributeInstance attributeInstance = entity.getAttributeInstance(configuration.attribute());
		return configuration.comparison().check(attributeInstance != null ? attributeInstance.getValue() : 0);
	}
}

