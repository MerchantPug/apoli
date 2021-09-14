package io.github.edwinmindcraft.apoli.api.power.configuration.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;

public interface IVariableIntPowerConfiguration extends IDynamicFeatureConfiguration {
	int min();

	int max();

	int initialValue();
}
