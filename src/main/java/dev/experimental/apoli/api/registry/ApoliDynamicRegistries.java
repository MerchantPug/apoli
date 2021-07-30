package dev.experimental.apoli.api.registry;

import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ApoliDynamicRegistries {
	public static final ResourceKey<Registry<ConfiguredPower<?, ?>>> CONFIGURED_POWER_KEY = ResourceKey.createRegistryKey(ApoliAPI.identifier("configured_powers"));
}
