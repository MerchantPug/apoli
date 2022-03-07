package io.github.edwinmindcraft.apoli.common.power;

import com.google.common.collect.ImmutableSet;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.MultipleConfiguration;

import java.util.Map;
import java.util.function.Predicate;

public class MultiplePower extends PowerFactory<MultipleConfiguration<ConfiguredPower<?, ?>>> {
	private static final ImmutableSet<String> EXCLUDED = ImmutableSet.of("type", "loading_priority", "name", "description", "hidden", "condition", "conditions");
	private static final Predicate<String> ALLOWED = str -> !EXCLUDED.contains(str);

	public MultiplePower() {
		super(MultipleConfiguration.mapCodec(ConfiguredPower.CODEC, ALLOWED, s -> "_" + s, MultiplePower::reconfigure).codec(), false);
	}

	@Override
	public Map<String, ConfiguredPower<?, ?>> getContainedPowers(ConfiguredPower<MultipleConfiguration<ConfiguredPower<?, ?>>, ?> configuration) {
		return configuration.getConfiguration().children();
	}

	/**
	 * Has the effect of hiding the power from the origin screen.
	 */
	private static <C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> ConfiguredPower<C, ?> reconfigure(ConfiguredPower<C, F> source) {
		return source.getFactory().configure(source.getConfiguration(), source.getData().copyOf().hidden().build());
	}
}
