package dev.experimental.apoli.api.registry;

import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ApoliDynamicRegistries {
	public static final RegistryKey<Registry<ConfiguredPower<?, ?>>> CONFIGURED_POWER_KEY = RegistryKey.ofRegistry(ApoliAPI.identifier("configured_powers"));
}
