package dev.experimental.apoli.common.registry;

import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.apace100.apoli.Apoli;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class ModRegistries {
	public static final Registries REGISTRIES = Registries.get(Apoli.MODID);
	public static final RegistrySupplier<Attribute> SWIM_SPEED = REGISTRIES.get(Registry.ATTRIBUTE_REGISTRY).delegate(new ResourceLocation("forge", "swim_speed"));
}
