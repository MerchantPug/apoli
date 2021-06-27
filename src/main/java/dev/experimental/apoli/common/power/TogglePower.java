package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.factory.power.TogglePowerFactory;
import dev.experimental.apoli.common.power.configuration.ToggleConfiguration;

public class TogglePower extends TogglePowerFactory.Simple<ToggleConfiguration> {

	public TogglePower() {
		super(ToggleConfiguration.CODEC);
	}
}
