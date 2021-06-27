package dev.experimental.apoli.api.power.configuration.power;

import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.IActivePower;

public interface ITogglePowerConfiguration extends IDynamicFeatureConfiguration {
	boolean defaultState();

	IActivePower.Key key();
}
