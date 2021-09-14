package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.AttributeComparisonConfiguration;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class AttributeCondition extends EntityCondition<AttributeComparisonConfiguration> {

	public AttributeCondition() {
		super(AttributeComparisonConfiguration.CODEC);
	}

	@Override
	public boolean check(AttributeComparisonConfiguration configuration, LivingEntity entity) {
		AttributeInstance attributeInstance = entity.getAttribute(configuration.attribute());
		return configuration.comparison().check(attributeInstance != null ? attributeInstance.getValue() : 0);
	}
}

