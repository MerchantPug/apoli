package dev.experimental.apoli.common.registry;

import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.apace100.apoli.Apoli;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRegistries {
	public static final Registries REGISTRIES = Registries.get(Apoli.MODID);
	public static final RegistrySupplier<EntityAttribute> SWIM_SPEED = REGISTRIES.get(Registry.ATTRIBUTE_KEY).delegate(new Identifier("forge", "swim_speed"));
}
