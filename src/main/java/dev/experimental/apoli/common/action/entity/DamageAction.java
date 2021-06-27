package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.common.action.configuration.DamageConfiguration;
import dev.experimental.apoli.api.power.factory.EntityAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class DamageAction extends EntityAction<DamageConfiguration> {

	public DamageAction() {
		super(DamageConfiguration.CODEC);
	}

	@Override
	public void execute(DamageConfiguration configuration, Entity entity) {
		if (entity instanceof LivingEntity)
			entity.damage(configuration.source(), configuration.amount());
	}
}
