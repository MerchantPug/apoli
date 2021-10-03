package io.github.edwinmindcraft.apoli.common.data;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.event.PowerLoadingEvent;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.calio.api.registry.DynamicEntryFactory;
import io.github.edwinmindcraft.calio.api.registry.DynamicEntryValidator;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public enum PowerLoader implements DynamicEntryFactory<ConfiguredPower<?, ?>>, DynamicEntryValidator<ConfiguredPower<?, ?>> {
	INSTANCE;

	private static final Comparator<ConfiguredPower<?, ?>> LOADING_ORDER_COMPARATOR = Comparator.comparingInt((ConfiguredPower<?, ?> x) -> x.getData().loadingPriority());

	public static String CURRENT_NAMESPACE;
	public static String CURRENT_PATH;

	@Override
	public ConfiguredPower<?, ?> accept(ResourceLocation resourceLocation, List<JsonElement> list) {
		CURRENT_NAMESPACE = resourceLocation.getNamespace();
		CURRENT_PATH = resourceLocation.getPath();
		Optional<ConfiguredPower<?, ?>> definition = list.stream().flatMap(x -> {
			DataResult<ConfiguredPower<?, ?>> power = ConfiguredPower.CODEC.decode(JsonOps.INSTANCE, x).map(Pair::getFirst);
			Optional<ConfiguredPower<?, ?>> powerDefinition = power.resultOrPartial(error -> Apoli.LOGGER.error("Error loading power \"{}\": {}", resourceLocation, error));
			return powerDefinition.stream();
		}).max(LOADING_ORDER_COMPARATOR);
		CURRENT_NAMESPACE = null;
		CURRENT_PATH = null;
		return definition.orElse(null);
	}

	@Override
	public DataResult<ConfiguredPower<?, ?>> validate(ResourceLocation location, ConfiguredPower<?, ?> configuredPower, ICalioDynamicRegistryManager manager) {
		if (configuredPower == null)
			return DataResult.error("Loading for all instances of this power failed.");
		if (!configuredPower.isConfigurationValid()) {
			configuredPower.getErrors(manager).forEach(x -> Apoli.LOGGER.error("Error in power {}: {}", location, x));
			return DataResult.error("Invalid Configuration.");
		}
		return DataResult.success(configuredPower);
	}
}
