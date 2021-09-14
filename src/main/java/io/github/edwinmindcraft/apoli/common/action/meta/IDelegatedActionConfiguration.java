package io.github.edwinmindcraft.apoli.common.action.meta;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;

public interface IDelegatedActionConfiguration<V> extends IDynamicFeatureConfiguration {
	void execute(V parameters);
}
