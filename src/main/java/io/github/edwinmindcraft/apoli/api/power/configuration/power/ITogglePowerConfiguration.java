package io.github.edwinmindcraft.apoli.api.power.configuration.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;

public interface ITogglePowerConfiguration extends IDynamicFeatureConfiguration {
	boolean defaultState();

	IActivePower.Key key();
}
