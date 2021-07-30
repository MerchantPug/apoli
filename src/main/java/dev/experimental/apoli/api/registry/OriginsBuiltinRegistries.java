package dev.experimental.apoli.api.registry;

import com.mojang.serialization.Lifecycle;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class OriginsBuiltinRegistries {
	public static final Registry<ConfiguredPower<?, ?>> CONFIGURED_POWERS = create(ApoliDynamicRegistries.CONFIGURED_POWER_KEY, Lifecycle.stable());

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static <T> Registry<T> create(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle) {
		return Registry.register((Registry) Registry.REGISTRY, key.location(), new MappedRegistry<>(key, lifecycle));
	}
}
