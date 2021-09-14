package io.github.edwinmindcraft.apoli.common.condition.damage;

import io.github.edwinmindcraft.apoli.api.configuration.FloatComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.DamageCondition;
import net.minecraft.world.damagesource.DamageSource;

public class AmountCondition extends DamageCondition<FloatComparisonConfiguration> {

	public AmountCondition() {
		super(FloatComparisonConfiguration.CODEC);
	}

	@Override
	protected boolean check(FloatComparisonConfiguration configuration, DamageSource source, float amount) {
		return configuration.check(amount);
	}
}
