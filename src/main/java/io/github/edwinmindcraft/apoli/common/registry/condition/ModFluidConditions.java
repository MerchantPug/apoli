package io.github.edwinmindcraft.apoli.common.registry.condition;

import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredFluidCondition;
import io.github.edwinmindcraft.apoli.common.condition.fluid.DelegatedFluidCondition;
import io.github.edwinmindcraft.apoli.common.condition.fluid.InTagFluidCondition;
import io.github.edwinmindcraft.apoli.common.condition.fluid.SimpleFluidCondition;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.FLUID_CONDITIONS;

public class ModFluidConditions {
	public static final BiPredicate<ConfiguredFluidCondition<?, ?>, FluidState> PREDICATE = (config, biome) -> config.check(biome);

	public static final RegistryObject<SimpleFluidCondition> EMPTY = FLUID_CONDITIONS.register("empty", () -> new SimpleFluidCondition(FluidState::isEmpty));
	public static final RegistryObject<SimpleFluidCondition> STILL = FLUID_CONDITIONS.register("still", () -> new SimpleFluidCondition(FluidState::isSource));
	public static final RegistryObject<InTagFluidCondition> IN_TAG = FLUID_CONDITIONS.register("in_tag", InTagFluidCondition::new);

	public static void register() {
		MetaFactories.defineMetaConditions(FLUID_CONDITIONS, DelegatedFluidCondition::new, ConfiguredFluidCondition.CODEC, PREDICATE);
	}
}
