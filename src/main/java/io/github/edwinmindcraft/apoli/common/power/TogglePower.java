package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.configuration.power.TogglePowerConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.power.TogglePowerFactory;

public class TogglePower extends TogglePowerFactory.Simple<TogglePowerConfiguration> {

	public TogglePower() {
		super(TogglePowerConfiguration.Impl.CODEC);
	}
}
