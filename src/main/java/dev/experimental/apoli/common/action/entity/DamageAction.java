package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.common.action.configuration.DamageConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import dev.experimental.apoli.api.power.factory.EntityAction;

public class DamageAction extends EntityAction<DamageConfiguration> {

	public DamageAction() {
		super(DamageConfiguration.CODEC);
	}

	@Override
	public void execute(DamageConfiguration configuration, Entity entity) {
		if (entity instanceof LivingEntity)
			entity.hurt(configuration.source(), configuration.amount());
	}
}
