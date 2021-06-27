package dev.experimental.apoli.common.registry.condition;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.experimental.apoli.api.MetaFactories;
import dev.experimental.apoli.api.power.configuration.ConfiguredFluidCondition;
import dev.experimental.apoli.api.power.factory.FluidCondition;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import dev.experimental.apoli.common.condition.fluid.DelegatedFluidCondition;
import dev.experimental.apoli.common.condition.fluid.InTagFluidCondition;
import dev.experimental.apoli.common.condition.fluid.SimpleFluidCondition;
import io.github.apace100.apoli.Apoli;
import net.minecraft.fluid.FluidState;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class ModFluidConditions {
	public static final BiPredicate<ConfiguredFluidCondition<?, ?>, FluidState> PREDICATE = (config, biome) -> config.check(biome);

	public static final RegistrySupplier<SimpleFluidCondition> EMPTY = register("empty", () -> new SimpleFluidCondition(FluidState::isEmpty));
	public static final RegistrySupplier<SimpleFluidCondition> STILL = register("still", () -> new SimpleFluidCondition(FluidState::isStill));
	public static final RegistrySupplier<InTagFluidCondition> IN_TAG = register("in_tag", InTagFluidCondition::new);

	public static void register() {
		MetaFactories.defineMetaConditions(ApoliRegistries.FLUID_CONDITION, DelegatedFluidCondition::new, ConfiguredFluidCondition.CODEC, PREDICATE);
	}

	private static <T extends FluidCondition<?>> RegistrySupplier<T> register(String name, Supplier<T> factory) {
		return ApoliRegistries.FLUID_CONDITION.register(Apoli.identifier(name), factory);
	}
}
