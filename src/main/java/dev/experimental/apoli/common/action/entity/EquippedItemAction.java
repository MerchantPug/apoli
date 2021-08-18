package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.power.factory.EntityAction;
import dev.experimental.apoli.common.action.configuration.EquippedItemConfiguration;
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
