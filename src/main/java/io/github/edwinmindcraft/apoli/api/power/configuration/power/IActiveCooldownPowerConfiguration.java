package io.github.edwinmindcraft.apoli.api.power.configuration.power;

import io.github.edwinmindcraft.apoli.api.power.IActivePower;

public interface IActiveCooldownPowerConfiguration extends ICooldownPowerConfiguration {
	IActivePower.Key key();
}
