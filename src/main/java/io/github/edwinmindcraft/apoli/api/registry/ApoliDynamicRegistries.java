package io.github.edwinmindcraft.apoli.api.registry;

import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ApoliDynamicRegistries {
	public static final ResourceKey<Registry<ConfiguredPower<?, ?>>> CONFIGURED_POWER_KEY = ResourceKey.createRegistryKey(ApoliAPI.identifier("configured_powers"));
}
