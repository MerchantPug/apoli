package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.power.TogglePowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ToggleConfiguration;

public class TogglePower extends TogglePowerFactory.Simple<ToggleConfiguration> {

	public TogglePower() {
		super(ToggleConfiguration.CODEC);
	}
}
