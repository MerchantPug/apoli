package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.AttributeConfiguration;
import net.minecraft.world.entity.Entity;

public class AttributePower extends PowerFactory<AttributeConfiguration> {
	public AttributePower() {
		super(AttributeConfiguration.CODEC, false);
	}

	@Override
	protected void onAdded(AttributeConfiguration configuration, Entity entity) {
		configuration.add(entity);
	}

	@Override
	protected void onRemoved(AttributeConfiguration configuration, Entity entity) {
		configuration.remove(entity);
	}
}
