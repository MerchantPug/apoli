package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.apace100.apoli.action.configuration.ExplodeConfiguration;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ExplosionDamageCalculator;

public class ExplodeAction extends EntityAction<ExplodeConfiguration> {
	public ExplodeAction() {
		super(ExplodeConfiguration.CODEC);
	}

	@Override
	public void execute(ExplodeConfiguration configuration, Entity entity) {
		if (entity.level.isClientSide())
			return;
		ExplosionDamageCalculator calculator = configuration.calculator();
		entity.level.explode(configuration.damageSelf() ? null : entity, DamageSource.explosion(entity instanceof LivingEntity ? (LivingEntity) entity : null), calculator, entity.getX(), entity.getY(), entity.getZ(), configuration.power(), configuration.createFire(), configuration.destructionType());
	}
}
