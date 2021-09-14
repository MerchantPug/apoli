package io.github.edwinmindcraft.apoli.common.condition.damage;

import io.github.edwinmindcraft.apoli.api.configuration.NoConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.DamageCondition;
import net.minecraft.world.damagesource.DamageSource;

public class FireDamageCondition extends DamageCondition<NoConfiguration> {
	public FireDamageCondition() {
		super(NoConfiguration.CODEC);
	}

	@Override
	protected boolean check(NoConfiguration configuration, DamageSource source, float amount) {
		return source.isFire();
	}
}
