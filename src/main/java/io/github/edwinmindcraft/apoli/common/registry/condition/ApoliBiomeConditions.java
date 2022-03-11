package io.github.edwinmindcraft.apoli.common.registry.condition;


import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiomeCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BiomeCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.biome.DelegatedBiomeCondition;
import io.github.edwinmindcraft.apoli.common.condition.biome.FloatComparingBiomeCondition;
import io.github.edwinmindcraft.apoli.common.condition.biome.HighHumidityCondition;
import io.github.edwinmindcraft.apoli.common.condition.biome.StringBiomeCondition;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConditionStreamConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConstantConfiguration;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.BIOME_CONDITIONS;

public class ApoliBiomeConditions {
	public static final BiPredicate<ConfiguredBiomeCondition<?, ?>, Biome> PREDICATE = (config, biome) -> config.check(biome);

	private static <U extends BiomeCondition<?>> RegistryObject<U> of(String name) {
		return RegistryObject.of(Apoli.identifier(name), ApoliRegistries.BIOME_CONDITION_CLASS, Apoli.MODID);
	}

	public static final RegistryObject<DelegatedBiomeCondition<ConstantConfiguration<Biome>>> CONSTANT = of("constant");
	public static final RegistryObject<DelegatedBiomeCondition<ConditionStreamConfiguration<ConfiguredBiomeCondition<?, ?>, Biome>>> AND = of("and");
	public static final RegistryObject<DelegatedBiomeCondition<ConditionStreamConfiguration<ConfiguredBiomeCondition<?, ?>, Biome>>> OR = of("or");

	public static final RegistryObject<StringBiomeCondition> CATEGORY = BIOME_CONDITIONS.register("category", () -> new StringBiomeCondition("category", biome -> biome.getBiomeCategory().getName()));
	public static final RegistryObject<HighHumidityCondition> HIGH_HUMIDITY = BIOME_CONDITIONS.register("high_humidity", HighHumidityCondition::new);
	public static final RegistryObject<StringBiomeCondition> PRECIPITATION = BIOME_CONDITIONS.register("precipitation", () -> new StringBiomeCondition("precipitation", biome -> biome.getBiomeCategory().getName()));
	public static final RegistryObject<FloatComparingBiomeCondition> TEMPERATURE = BIOME_CONDITIONS.register("temperature", () -> new FloatComparingBiomeCondition(Biome::getBaseTemperature));

	public static ConfiguredBiomeCondition<?, ?> constant(boolean value) {return CONSTANT.get().configure(new ConstantConfiguration<>(value));}

	public static ConfiguredBiomeCondition<?, ?> and(ConfiguredBiomeCondition<?, ?>... conditions) {return AND.get().configure(ConditionStreamConfiguration.and(Arrays.asList(conditions), PREDICATE));}

	public static ConfiguredBiomeCondition<?, ?> or(ConfiguredBiomeCondition<?, ?>... conditions) {return OR.get().configure(ConditionStreamConfiguration.or(Arrays.asList(conditions), PREDICATE));}

	public static void bootstrap() {
		MetaFactories.defineMetaConditions(BIOME_CONDITIONS, DelegatedBiomeCondition::new, ConfiguredBiomeCondition.CODEC, PREDICATE);
	}
}
