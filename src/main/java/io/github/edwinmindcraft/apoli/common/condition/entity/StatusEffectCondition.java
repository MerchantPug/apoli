package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.StatusEffectConfiguration;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class StatusEffectCondition extends EntityCondition<StatusEffectConfiguration> {
	public StatusEffectCondition() {
		super(StatusEffectConfiguration.CODEC);
	}

	@Override
	public boolean check(StatusEffectConfiguration configuration, Entity entity) {
		if (!(entity instanceof LivingEntity living) || !living.hasEffect(configuration.effect()))
			return false;
		MobEffectInstance instance = living.getEffect(configuration.effect());
		assert instance != null;
		return instance.getDuration() <= configuration.maxDuration() && instance.getDuration() >= configuration.minDuration()
			   && instance.getAmplifier() <= configuration.maxAmplifier() && instance.getAmplifier() >= configuration.minAmplifier();
	}
}
