package io.github.edwinmindcraft.apoli.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.registry.ApoliDynamicRegistries;
import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.calio.api.CalioAPI;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * A global configuration class for all origin features, containing some useful
 * utility methods to help with coding and verification.
 */
public interface IDynamicFeatureConfiguration {
	static void populate(Map<String, IDynamicFeatureConfiguration> builder, Iterable<?> iterable, String prefix) {
		int i = 0;
		for (Object o : iterable) {
			if (o instanceof IDynamicFeatureConfiguration config)
				builder.put(prefix + i, config);
			else if (o instanceof Map<?, ?> map)
				populate(builder, map, prefix + i + "/");
			else if (o instanceof Iterable<?> iterable2)
				populate(builder, iterable2, prefix + i + "/");
			++i;
		}
	}

	static void populate(Map<String, IDynamicFeatureConfiguration> builder, Map<?, ?> map, String prefix) {
		map.forEach((o, o2) -> {
			String key = "?";
			if (o instanceof String str) key = str;
			else if (o instanceof StringRepresentable identifiable) key = identifiable.getSerializedName();
			if (o2 instanceof IDynamicFeatureConfiguration config)
				builder.put(prefix + key, config);
			else if (o2 instanceof Map<?, ?> map2)
				populate(builder, map2, prefix + key + "/");
			else if (o2 instanceof Iterable<?> iterable)
				populate(builder, iterable, prefix + key + "/");
		});
	}

	/**
	 * Checks if this configuration is valid.
	 *
	 * @return The errors that invalidate this configuration, or an empty list in no errors where found.
	 */
	@NotNull
	default List<String> getErrors(@NotNull ICalioDynamicRegistryManager server) {
		return this.getChildrenComponent().entrySet().stream().flatMap(entry -> this.copyErrorsFrom(entry.getValue(), server, this.name(), entry.getKey()).stream()).toList();
	}

	/**
	 * Returns a list of all warnings that appear during the configuration of this feature.<br/>
	 * If something that really should be here is missing, but it was marked as optional, this
	 * should be written here.
	 */
	@NotNull
	default List<String> getWarnings(@NotNull ICalioDynamicRegistryManager server) {
		return this.getChildrenComponent().entrySet().stream().flatMap(entry -> this.copyWarningsFrom(entry.getValue(), server, this.name(), entry.getKey()).stream()).toList();
	}

	@NotNull
	default String name() {
		String name = this.getClass().getSimpleName();
		if (name.endsWith("Configuration"))
			return name.substring(0, name.length() - "Configuration".length());
		return name;
	}

	@NotNull
	default Map<String, IDynamicFeatureConfiguration> getChildrenComponent() {
		if (this instanceof Record record) {
			Map<String, IDynamicFeatureConfiguration> builder = new HashMap<>();
			for (RecordComponent component : record.getClass().getRecordComponents()) {
				try {
					Object invoke = component.getAccessor().invoke(record);
					if (invoke instanceof IDynamicFeatureConfiguration config)
						builder.put(component.getName(), config);
					else if (invoke instanceof Map<?, ?> map)
						populate(builder, map, component.getName() + "/");
					else if (invoke instanceof Iterable<?> iterable)
						populate(builder, iterable, component.getName() + "/");
				} catch (IllegalAccessException | InvocationTargetException e) {
					Apoli.LOGGER.warn("Failed to access record component \"{}\" for \"{}\", auto logging may fail.", component, this.name());
					Apoli.LOGGER.debug(e);
				}
			}
			return ImmutableMap.copyOf(builder);
		}
		return ImmutableMap.of();
	}

	/**
	 * This is used to check whether this configuration is valid. i.e. if there is a point in executing the power.
	 */
	default boolean isConfigurationValid() { return true; }

	/**
	 * Returns a list of powers that are not registered in the dynamic registry.
	 *
	 * @param dynamicRegistryManager The dynamic registry manager, use {@link CalioAPI#getDynamicRegistries(MinecraftServer)} to access it.
	 * @param identifiers            The powers to check the existence of.
	 *
	 * @return A containing all the missing powers.
	 */
	@NotNull
	default List<ResourceLocation> checkPower(@NotNull ICalioDynamicRegistryManager dynamicRegistryManager, @NotNull ResourceLocation... identifiers) {
		Registry<ConfiguredPower<?, ?>> powers = dynamicRegistryManager.get(ApoliDynamicRegistries.CONFIGURED_POWER_KEY);
		ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
		for (ResourceLocation identifier : identifiers) {
			if (!powers.containsKey(identifier))
				builder.add(identifier);
		}
		return builder.build();
	}

	default UnaryOperator<String> fieldName(String name, String... fields) {
		StringBuilder key = new StringBuilder(name);
		Arrays.stream(fields).flatMap(x -> Arrays.stream(x.split("/"))).filter(x -> !StringUtils.isBlank(x)).forEach(s -> key.append("[").append(s).append("]"));
		String val = key.append("/%s").toString();
		return val::formatted;
	}

	default List<String> copyErrorsFrom(@Nullable IDynamicFeatureConfiguration config, ICalioDynamicRegistryManager server, String name, String... fields) {
		if (config == null) return ImmutableList.of();
		return config.getErrors(server).stream().map(this.fieldName(name, fields)).toList();
	}

	default List<String> copyWarningsFrom(@Nullable IDynamicFeatureConfiguration config, ICalioDynamicRegistryManager server, String name, String... fields) {
		if (config == null) return ImmutableList.of();
		return config.getWarnings(server).stream().map(this.fieldName(name, fields)).toList();
	}
}