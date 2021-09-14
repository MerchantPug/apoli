package io.github.apace100.apoli.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.event.PowerLoadingEvent;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliDynamicRegistries;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.calio.data.MultiJsonDataLoader;
import io.github.edwinmindcraft.calio.api.CalioAPI;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PowerLoader extends MultiJsonDataLoader {
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private static final Comparator<ConfiguredPower<?, ?>> LOADING_ORDER_COMPARATOR = Comparator.comparingInt((ConfiguredPower<?, ?> x) -> x.getData().loadingPriority());

	/**
	 * Recursively registers all powers in the given file.
	 *
	 * @param registry   The registry to register the powers into.
	 * @param identifier The identifier of the power to register.
	 * @param original   The original {@link ConfiguredPower} to register.
	 */
	@SuppressWarnings("unchecked")
	private static void register(WritableRegistry<ConfiguredPower<?, ?>> registry, ResourceLocation identifier, ConfiguredPower<?, ?> original) {
		PowerLoadingEvent event = new PowerLoadingEvent(identifier, original, original.getData().complete(identifier));
		MinecraftForge.EVENT_BUS.post(event);
		if (!event.isCanceled()) {
			ConfiguredPower<?, ?> newPower = ((PowerFactory<IDynamicFeatureConfiguration>) original.getFactory()).configure(event.getOriginal().getConfiguration(), event.getBuilder().build());
			registry.register(ResourceKey.create(registry.key(), identifier), newPower, Lifecycle.experimental());
			newPower.getContainedPowers().forEach((s, configuredPower) -> register(registry, new ResourceLocation(identifier.getNamespace(), identifier.getPath() + s), configuredPower));
		}
	}

	public static String CURRENT_NAMESPACE;
	public static String CURRENT_PATH;

	public PowerLoader() {
		super(GSON, "powers");
	}

	@Override
	protected void apply(@NotNull Map<ResourceLocation, List<JsonElement>> map, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
		ICalioDynamicRegistryManager registryManager = CalioAPI.getDynamicRegistries();
		if (registryManager == null)
			throw new IllegalStateException("Tried to load powers before initializing dynamic registries!");
		WritableRegistry<ConfiguredPower<?, ?>> powers = registryManager.get(ApoliDynamicRegistries.CONFIGURED_POWER_KEY);
		map.forEach((identifier, jsonElements) -> {
			CURRENT_NAMESPACE = identifier.getNamespace();
			CURRENT_PATH = identifier.getPath();
			Optional<ConfiguredPower<?, ?>> definition = jsonElements.stream().flatMap(x -> {
				DataResult<ConfiguredPower<?, ?>> power = ConfiguredPower.CODEC.decode(JsonOps.INSTANCE, x).map(Pair::getFirst);
				Optional<ConfiguredPower<?, ?>> powerDefinition = power.resultOrPartial(error -> Apoli.LOGGER.error("Error loading power \"{}\": {}", identifier, error));
				return powerDefinition.stream();
			}).max(LOADING_ORDER_COMPARATOR);
			//This may be breaking because subpowers of overridden powers will no longer be registered.
			//Fixing it should be easy, but that would be bad design, so not doing it.
			definition.ifPresentOrElse(def -> register(powers, identifier, def),
					() -> Apoli.LOGGER.error("Loading for all instances of power \"{}\" failed. It won't be registered", identifier));
		});
		CURRENT_NAMESPACE = null;
		CURRENT_PATH = null;
	}
}
