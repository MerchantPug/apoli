package dev.experimental.apoli.api.registry;

import com.mojang.serialization.Lifecycle;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public class OriginsBuiltinRegistries {
	public static final Registry<ConfiguredPower<?, ?>> CONFIGURED_POWERS = create(ApoliDynamicRegistries.CONFIGURED_POWER_KEY, Lifecycle.stable());

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle) {
		return Registry.register((Registry) Registry.REGISTRIES, key.getValue(), new SimpleRegistry<>(key, lifecycle));
	}
}
