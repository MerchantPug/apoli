package io.github.edwinmindcraft.apoli.common.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.calio.data.SerializableData;
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
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public enum PowerLoader implements DynamicEntryFactory<ConfiguredPower<?, ?>>, DynamicEntryValidator<ConfiguredPower<?, ?>> {
	INSTANCE;

	private static final Comparator<ConfiguredPower<?, ?>> LOADING_ORDER_COMPARATOR = Comparator.comparingInt((ConfiguredPower<?, ?> x) -> x.getData().loadingPriority());

	@Override
	public ConfiguredPower<?, ?> accept(ResourceLocation resourceLocation, List<JsonElement> list) {
		SerializableData.CURRENT_NAMESPACE = resourceLocation.getNamespace();
		SerializableData.CURRENT_PATH = resourceLocation.getPath();
		Optional<ConfiguredPower<?, ?>> definition = list.stream().flatMap(x -> {
			DataResult<ConfiguredPower<?, ?>> power = ConfiguredPower.CODEC.decode(JsonOps.INSTANCE, x).map(Pair::getFirst);
			Optional<ConfiguredPower<?, ?>> powerDefinition = power.resultOrPartial(error -> Apoli.LOGGER.error("Error loading power \"{}\": {}", resourceLocation, error));
			return powerDefinition.stream();
		}).max(LOADING_ORDER_COMPARATOR);
		SerializableData.CURRENT_NAMESPACE = null;
		SerializableData.CURRENT_PATH = null;
		if (definition.isEmpty())
			Apoli.LOGGER.error("Loading for all instances of power {} failed.", resourceLocation);
		return definition.orElse(null);
	}

	@Override
	public @NotNull Map<ResourceLocation, ConfiguredPower<?, ?>> create(ResourceLocation location, @NotNull List<JsonElement> entries) {
		ConfiguredPower<?, ?> accept = this.accept(location, entries);
		if (accept != null) {
			ImmutableMap.Builder<ResourceLocation, ConfiguredPower<?, ?>> builder = ImmutableMap.builder();
			//On a scale from 1 to derp, this is a derp.
			//The code was there, I just forgot to call it.
			builder.put(location, accept.complete(location));
			accept.getContainedPowers().forEach((s, configuredPower) -> {
				ResourceLocation path = new ResourceLocation(location.getNamespace(), location.getPath() + s);
				builder.put(path, configuredPower.complete(path));
			});
			return builder.build();
		}
		return ImmutableMap.of();
	}


	@Override
	public @NotNull DataResult<ConfiguredPower<?, ?>> validate(@NotNull ResourceLocation location, @NotNull ConfiguredPower<?, ?> configuredPower, @NotNull ICalioDynamicRegistryManager manager) {
		if (!configuredPower.isConfigurationValid()) {
			configuredPower.getErrors(manager).forEach(x -> Apoli.LOGGER.error("Error in power {}: {}", location, x));
			return DataResult.error("Invalid Configuration.");
		}
		return DataResult.success(configuredPower);
	}
}
