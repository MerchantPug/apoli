package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.INightVisionPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.power.TogglePowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ToggleNightVisionConfiguration;
import net.minecraft.world.entity.Entity;

public class ToggleNightVisionPower extends TogglePowerFactory.Simple<ToggleNightVisionConfiguration> implements INightVisionPower<ToggleNightVisionConfiguration> {

	public ToggleNightVisionPower() {
		super(ToggleNightVisionConfiguration.CODEC);
	}

	@Override
	public float getStrength(ConfiguredPower<ToggleNightVisionConfiguration, ?> configuration, Entity player) {
		return configuration.getConfiguration().strength();
	}
}
