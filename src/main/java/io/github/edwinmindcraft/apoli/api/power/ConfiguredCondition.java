package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;

public abstract class ConfiguredCondition<T extends IDynamicFeatureConfiguration, F> extends ConfiguredFactory<T, F> {
	private final ConditionData data;

	protected ConfiguredCondition(F factory, T configuration, ConditionData data) {
		super(factory, configuration);
		this.data = data;
	}

	public ConditionData getData() {
		return this.data;
	}
}
