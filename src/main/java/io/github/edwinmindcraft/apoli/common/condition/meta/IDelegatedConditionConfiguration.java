package io.github.edwinmindcraft.apoli.common.condition.meta;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;

public interface IDelegatedConditionConfiguration<V> extends IDynamicFeatureConfiguration {
	boolean check(V parameters);
}
