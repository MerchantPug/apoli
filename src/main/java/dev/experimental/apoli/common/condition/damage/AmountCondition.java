package dev.experimental.apoli.common.condition.damage;

import dev.experimental.apoli.api.configuration.FloatComparisonConfiguration;
import dev.experimental.apoli.api.power.factory.DamageCondition;
import net.minecraft.entity.damage.DamageSource;

public class AmountCondition extends DamageCondition<FloatComparisonConfiguration> {

	public AmountCondition() {
		super(FloatComparisonConfiguration.CODEC);
	}

	@Override
	protected boolean check(FloatComparisonConfiguration configuration, DamageSource source, float amount) {
		return configuration.check(amount);
	}
}
