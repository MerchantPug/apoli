package dev.experimental.apoli.api.power.configuration.power;

import dev.experimental.apoli.api.power.IActivePower;

public interface IActiveCooldownPowerConfiguration extends ICooldownPowerConfiguration {
	IActivePower.Key key();
}
