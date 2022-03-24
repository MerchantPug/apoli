package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;

import java.util.function.Supplier;

public abstract class ConfiguredCondition<T extends IDynamicFeatureConfiguration, F, CF extends ConfiguredCondition<?, ?, CF>> extends ConfiguredFactory<T, F, CF> {
	private final ConditionData data;

	protected ConfiguredCondition(Supplier<F> factory, T configuration, ConditionData data) {
		super(factory, configuration);
		this.data = data;
	}

	public ConditionData getData() {
		return this.data;
	}
}
