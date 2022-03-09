package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.RestrictArmorConfiguration;
import net.minecraft.world.entity.Entity;

public class ConditionedRestrictArmorPower extends PowerFactory<RestrictArmorConfiguration> {
	public ConditionedRestrictArmorPower() {
		super(RestrictArmorConfiguration.CODEC);
		this.ticking();
	}

	@Override
	protected void tick(RestrictArmorConfiguration configuration, Entity player) {
		configuration.dropIllegalItems(player);
	}

	@Override
	protected int tickInterval(RestrictArmorConfiguration configuration, Entity player) {
		return configuration.tickRate();
	}
}
