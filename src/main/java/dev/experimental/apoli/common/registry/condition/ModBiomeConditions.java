package dev.experimental.apoli.common.registry.condition;


import dev.architectury.registry.registries.RegistrySupplier;
import dev.experimental.apoli.api.MetaFactories;
import dev.experimental.apoli.api.power.configuration.ConfiguredBiomeCondition;
import dev.experimental.apoli.api.power.factory.BiomeCondition;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import dev.experimental.apoli.common.condition.biome.DelegatedBiomeCondition;
import dev.experimental.apoli.common.condition.biome.FloatComparingBiomeCondition;
import dev.experimental.apoli.common.condition.biome.HighHumidityCondition;
import dev.experimental.apoli.common.condition.biome.StringBiomeCondition;
import io.github.apace100.apoli.Apoli;
import net.minecraft.world.biome.Biome;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class ModBiomeConditions {
	public static final BiPredicate<ConfiguredBiomeCondition<?, ?>, Biome> PREDICATE = (config, biome) -> config.check(biome);

	public static final RegistrySupplier<StringBiomeCondition> CATEGORY = register("category", () -> new StringBiomeCondition("category", biome -> biome.getCategory().getName()));
	public static final RegistrySupplier<HighHumidityCondition> HIGH_HUMIDITY = register("high_humidity", HighHumidityCondition::new);
	public static final RegistrySupplier<StringBiomeCondition> PRECIPITATION = register("precipitation", () -> new StringBiomeCondition("precipitation", biome -> biome.getCategory().getName()));
	public static final RegistrySupplier<FloatComparingBiomeCondition> TEMPERATURE = register("temperature", () -> new FloatComparingBiomeCondition(Biome::getTemperature));

	public static void register() {
		MetaFactories.defineMetaConditions(ApoliRegistries.BIOME_CONDITION, DelegatedBiomeCondition::new, ConfiguredBiomeCondition.CODEC, PREDICATE);
	}

	private static <T extends BiomeCondition<?>> RegistrySupplier<T> register(String name, Supplier<T> factory) {
		return ApoliRegistries.BIOME_CONDITION.register(Apoli.identifier(name), factory);
	}
}
