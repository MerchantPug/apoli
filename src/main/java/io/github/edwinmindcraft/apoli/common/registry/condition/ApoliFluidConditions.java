package io.github.edwinmindcraft.apoli.common.registry.condition;

import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiomeCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredFluidCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.api.power.factory.FluidCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.entity.DelegatedEntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.fluid.DelegatedFluidCondition;
import io.github.edwinmindcraft.apoli.common.condition.fluid.InTagFluidCondition;
import io.github.edwinmindcraft.apoli.common.condition.fluid.SimpleFluidCondition;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConditionStreamConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConstantConfiguration;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.Arrays;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.FLUID_CONDITIONS;

public class ApoliFluidConditions {
	public static final BiPredicate<ConfiguredFluidCondition<?, ?>, FluidState> PREDICATE = (config, biome) -> config.check(biome);

	private static <U extends FluidCondition<?>> RegistryObject<U> of(String name) {
		return RegistryObject.of(Apoli.identifier(name), ApoliRegistries.FLUID_CONDITION_CLASS, Apoli.MODID);
	}

	public static final RegistryObject<DelegatedFluidCondition<ConstantConfiguration<FluidState>>> CONSTANT = of("constant");
	public static final RegistryObject<DelegatedFluidCondition<ConditionStreamConfiguration<ConfiguredFluidCondition<?, ?>, FluidState>>> AND = of("and");
	public static final RegistryObject<DelegatedFluidCondition<ConditionStreamConfiguration<ConfiguredFluidCondition<?, ?>, FluidState>>> OR = of("or");

	public static final RegistryObject<SimpleFluidCondition> EMPTY = FLUID_CONDITIONS.register("empty", () -> new SimpleFluidCondition(FluidState::isEmpty));
	public static final RegistryObject<SimpleFluidCondition> STILL = FLUID_CONDITIONS.register("still", () -> new SimpleFluidCondition(FluidState::isSource));
	public static final RegistryObject<InTagFluidCondition> IN_TAG = FLUID_CONDITIONS.register("in_tag", InTagFluidCondition::new);

	public static ConfiguredFluidCondition<?, ?> constant(boolean value) {return CONSTANT.get().configure(new ConstantConfiguration<>(value));}

	public static ConfiguredFluidCondition<?, ?> and(ConfiguredFluidCondition<?, ?>... conditions) {return AND.get().configure(ConditionStreamConfiguration.and(Arrays.asList(conditions), PREDICATE));}

	public static ConfiguredFluidCondition<?, ?> or(ConfiguredFluidCondition<?, ?>... conditions) {return OR.get().configure(ConditionStreamConfiguration.or(Arrays.asList(conditions), PREDICATE));}

	public static void register() {
		MetaFactories.defineMetaConditions(FLUID_CONDITIONS, DelegatedFluidCondition::new, ConfiguredFluidCondition.CODEC, PREDICATE);
	}
}
