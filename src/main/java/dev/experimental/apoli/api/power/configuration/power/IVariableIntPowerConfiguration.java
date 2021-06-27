package dev.experimental.apoli.api.power.configuration.power;

import dev.experimental.apoli.api.IDynamicFeatureConfiguration;

public interface IVariableIntPowerConfiguration extends IDynamicFeatureConfiguration {
	int min();

	int max();

	int initialValue();
}
