package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.TagConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;

public class InTagCondition extends EntityCondition<TagConfiguration<EntityType<?>>> {

	public InTagCondition() {
		super(TagConfiguration.codec(SerializableDataTypes.ENTITY_TAG, "tag"));
	}

	@Override
	public boolean check(TagConfiguration<EntityType<?>> configuration, LivingEntity entity) {
		return configuration.contains(entity.getType());
	}
}
