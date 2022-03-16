package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ConfiguredFactory<T extends IDynamicFeatureConfiguration, F> implements IDynamicFeatureConfiguration {
	private final F factory;
	private final T configuration;

	protected ConfiguredFactory(F factory, T configuration) {
		this.factory = factory;
		this.configuration = configuration;
	}

	public F getFactory() {
		return this.factory;
	}

	public T getConfiguration() {
		return this.configuration;
	}

	@Override
	public @NotNull List<String> getErrors(@NotNull ICalioDynamicRegistryManager server) {
		return this.getConfiguration().getErrors(server);
	}

	@Override
	public @NotNull List<String> getWarnings(@NotNull ICalioDynamicRegistryManager server) {
		return this.getConfiguration().getWarnings(server);
	}

	@Override
	public boolean isConfigurationValid() {
		return this.getConfiguration().isConfigurationValid();
	}
}
