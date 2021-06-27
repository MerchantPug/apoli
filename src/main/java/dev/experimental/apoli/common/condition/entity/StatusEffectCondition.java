package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.common.condition.configuration.StatusEffectConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

public class StatusEffectCondition extends EntityCondition<StatusEffectConfiguration> {
	public StatusEffectCondition() {
		super(StatusEffectConfiguration.CODEC);
	}

	@Override
	public boolean check(StatusEffectConfiguration configuration, LivingEntity entity) {
		if (configuration.effect() == null || !entity.hasStatusEffect(configuration.effect()))
			return false;
		StatusEffectInstance instance = entity.getStatusEffect(configuration.effect());
		assert instance != null;
		return instance.getDuration() <= configuration.maxDuration() && instance.getDuration() >= configuration.minDuration()
			   && instance.getAmplifier() <= configuration.maxAmplifier() && instance.getAmplifier() >= configuration.minAmplifier();
	}
}
