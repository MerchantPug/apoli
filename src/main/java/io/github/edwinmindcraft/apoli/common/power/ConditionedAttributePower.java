package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ConditionedAttributeConfiguration;
import net.minecraft.world.entity.Entity;

public class ConditionedAttributePower extends PowerFactory<ConditionedAttributeConfiguration> {
	public ConditionedAttributePower() {
		super(ConditionedAttributeConfiguration.CODEC);
		this.ticking(true);
	}

	@Override
	public void tick(ConfiguredPower<ConditionedAttributeConfiguration, ?> configuration, Entity player) {
		if (configuration.isActive(player))
			configuration.getConfiguration().attributes().add(player);
		else
			configuration.getConfiguration().attributes().remove(player);
	}

	@Override
	protected void onRemoved(ConditionedAttributeConfiguration configuration, Entity player) {
		configuration.attributes().remove(player);
	}

	@Override
	protected int tickInterval(ConditionedAttributeConfiguration configuration, Entity player) {
		return configuration.tickRate();
	}
}
