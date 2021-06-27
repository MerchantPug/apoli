package dev.experimental.apoli.common.condition.meta;

import dev.experimental.apoli.api.IDynamicFeatureConfiguration;

public interface IDelegatedConditionConfiguration<V> extends IDynamicFeatureConfiguration {
	boolean check(V parameters);
}
