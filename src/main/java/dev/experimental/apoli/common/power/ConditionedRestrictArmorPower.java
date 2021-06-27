package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.RestrictArmorConfiguration;
import net.minecraft.entity.LivingEntity;

public class ConditionedRestrictArmorPower extends PowerFactory<RestrictArmorConfiguration> {
	public ConditionedRestrictArmorPower() {
		super(RestrictArmorConfiguration.CODEC);
		this.ticking();
	}

	@Override
	protected void tick(RestrictArmorConfiguration configuration, LivingEntity player) {
		configuration.dropIllegalItems(player);
	}

	@Override
	protected int tickInterval(RestrictArmorConfiguration configuration, LivingEntity player) {
		return configuration.tickRate();
	}
}
