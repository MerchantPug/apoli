package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.EquippedItemConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EquippedItemAction extends EntityAction<EquippedItemConfiguration> {

	public EquippedItemAction() {
		super(EquippedItemConfiguration.CODEC);
	}

	@Override
	public void execute(EquippedItemConfiguration configuration, Entity entity) {
		if (entity instanceof LivingEntity living)
			configuration.action().execute(living.getItemBySlot(configuration.slot()));
	}
}
