package io.github.edwinmindcraft.apoli.api.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;

/**
 * Condition factories provide two methods for check.<br/>
 * Most of the time you won't need to override the version with {@link ConditionData},
 * but it may be useful to optimize the code.
 */
public interface IConditionFactory<T extends IDynamicFeatureConfiguration, C extends ConfiguredCondition<T, ? extends F, ?>, F extends IConditionFactory<T, C, F>> extends IFactory<T, C, F> {
	static <T extends IDynamicFeatureConfiguration, C extends ConfiguredCondition<T, ? extends F, ?>, F extends IConditionFactory<T, C, F>> Codec<C> conditionCodec(Codec<T> codec, F factory) {
		return IFactory.unionCodec(IFactory.asMap(codec), ConditionData.CODEC, factory::configure, ConfiguredCondition::getConfiguration, ConfiguredCondition::getData);
	}

	Codec<C> getConditionCodec();

	default C configure(T input) {
		return this.configure(input, ConditionData.DEFAULT);
	}

	C configure(T input, ConditionData configuration);
}
