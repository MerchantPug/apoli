package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.edwinmindcraft.apoli.common.action.configuration.SpawnEffectCloudConfiguration;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;

public class SpawnEffectCloudAction extends EntityAction<SpawnEffectCloudConfiguration> {

	public SpawnEffectCloudAction() {
		super(SpawnEffectCloudConfiguration.CODEC);
	}

	@Override
	public void execute(SpawnEffectCloudConfiguration configuration, Entity entity) {
		AreaEffectCloud areaEffectCloudEntity = new AreaEffectCloud(entity.level, entity.getX(), entity.getY(), entity.getZ());
		if (entity instanceof LivingEntity)
			areaEffectCloudEntity.setOwner((LivingEntity) entity);
		areaEffectCloudEntity.setRadius(configuration.radius());
		areaEffectCloudEntity.setRadiusOnUse(configuration.radiusOnUse());
		areaEffectCloudEntity.setWaitTime(configuration.waitTime());
		areaEffectCloudEntity.setRadiusPerTick(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
		configuration.effects().getContent().stream().map(MobEffectInstance::new).forEach(areaEffectCloudEntity::addEffect);
		entity.level.addFreshEntity(areaEffectCloudEntity);
	}
}
