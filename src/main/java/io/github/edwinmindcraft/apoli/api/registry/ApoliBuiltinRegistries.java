package io.github.edwinmindcraft.apoli.api.registry;

import io.github.apace100.calio.ClassUtil;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ApoliBuiltinRegistries {
	public static final Class<ConfiguredPower<?, ?>> CONFIGURED_POWER_CLASS = ClassUtil.get();
	public static Supplier<IForgeRegistry<ConfiguredPower<?, ?>>> CONFIGURED_POWERS;
}
