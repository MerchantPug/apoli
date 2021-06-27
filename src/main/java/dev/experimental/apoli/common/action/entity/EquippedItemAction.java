package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.common.action.configuration.EquippedItemConfiguration;
import dev.experimental.apoli.api.power.factory.EntityAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class EquippedItemAction extends EntityAction<EquippedItemConfiguration> {

	public EquippedItemAction() {
		super(EquippedItemConfiguration.CODEC);
	}

	@Override
	public void execute(EquippedItemConfiguration configuration, Entity entity) {
		if (entity instanceof LivingEntity living)
			configuration.action().execute(living.getEquippedStack(configuration.slot()));
	}
}
