package dev.experimental.apoli.common.action.meta;

import dev.experimental.apoli.api.IDynamicFeatureConfiguration;

public interface IDelegatedActionConfiguration<V> extends IDynamicFeatureConfiguration {
	void execute(V parameters);
}
