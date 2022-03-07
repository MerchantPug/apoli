package io.github.edwinmindcraft.apoli.common.registry.condition;

import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.block.*;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConditionStreamConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConstantConfiguration;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.BLOCK_CONDITIONS;

public class ApoliBlockConditions {
	public static final BiPredicate<ConfiguredBlockCondition<?, ?>, BlockInWorld> PREDICATE = (config, position) -> config.check(position);

	private static <U extends BlockCondition<?>> RegistryObject<U> of(String name) {
		return RegistryObject.of(Apoli.identifier(name), ApoliRegistries.BLOCK_CONDITION_CLASS, Apoli.MODID);
	}

	public static final RegistryObject<DelegatedBlockCondition<ConstantConfiguration<BlockInWorld>>> CONSTANT = of("constant");
	public static final RegistryObject<DelegatedBlockCondition<ConditionStreamConfiguration<ConfiguredBlockCondition<?, ?>, BlockInWorld>>> AND = of("and");
	public static final RegistryObject<DelegatedBlockCondition<ConditionStreamConfiguration<ConfiguredBlockCondition<?, ?>, BlockInWorld>>> OR = of("or");

	public static final RegistryObject<SimpleBlockCondition> MOVEMENT_BLOCKING = BLOCK_CONDITIONS.register("movement_blocking", () -> new SimpleBlockCondition(SimpleBlockCondition.MOVEMENT_BLOCKING));
	public static final RegistryObject<SimpleBlockCondition> REPLACEABLE_LEGACY = BLOCK_CONDITIONS.register("replacable", () -> new SimpleBlockCondition(SimpleBlockCondition.REPLACEABLE)); //This one has a typo.
	public static final RegistryObject<SimpleBlockCondition> REPLACEABLE = BLOCK_CONDITIONS.register("replaceable", () -> new SimpleBlockCondition(SimpleBlockCondition.REPLACEABLE)); //This one doesn't have a typo.
	public static final RegistryObject<SimpleBlockCondition> LIGHT_BLOCKING = BLOCK_CONDITIONS.register("light_blocking", () -> new SimpleBlockCondition(SimpleBlockCondition.LIGHT_BLOCKING));
	public static final RegistryObject<SimpleBlockCondition> WATER_LOGGABLE = BLOCK_CONDITIONS.register("water_loggable", () -> new SimpleBlockCondition(SimpleBlockCondition.WATER_LOGGABLE));
	public static final RegistryObject<SimpleBlockCondition> EXPOSED_TO_SKY = BLOCK_CONDITIONS.register("exposed_to_sky", () -> new SimpleBlockCondition(SimpleBlockCondition.EXPOSED_TO_SKY));
	public static final RegistryObject<InTagBlockCondition> IN_TAG = BLOCK_CONDITIONS.register("in_tag", InTagBlockCondition::new);
	public static final RegistryObject<FluidBlockCondition> FLUID = BLOCK_CONDITIONS.register("fluid", FluidBlockCondition::new);
	public static final RegistryObject<OffsetCondition> OFFSET = BLOCK_CONDITIONS.register("offset", OffsetCondition::new);
	public static final RegistryObject<AttachableCondition> ATTACHABLE = BLOCK_CONDITIONS.register("attachable", AttachableCondition::new);
	public static final RegistryObject<BlockTypeCondition> BLOCK = BLOCK_CONDITIONS.register("block", BlockTypeCondition::new);
	public static final RegistryObject<AdjacentCondition> ADJACENT = BLOCK_CONDITIONS.register("adjacent", AdjacentCondition::new);
	public static final RegistryObject<LightLevelCondition> LIGHT_LEVEL = BLOCK_CONDITIONS.register("light_level", LightLevelCondition::new);
	public static final RegistryObject<BlockStateCondition> BLOCK_STATE = BLOCK_CONDITIONS.register("block_state", BlockStateCondition::new);
	public static final RegistryObject<HeightCondition> HEIGHT = BLOCK_CONDITIONS.register("height", HeightCondition::new);

	public static ConfiguredBlockCondition<?, ?> constant(boolean value) {return CONSTANT.get().configure(new ConstantConfiguration<>(value));}

	public static ConfiguredBlockCondition<?, ?> and(ConfiguredBlockCondition<?, ?>... conditions) {return AND.get().configure(ConditionStreamConfiguration.and(Arrays.asList(conditions), PREDICATE));}

	public static ConfiguredBlockCondition<?, ?> or(ConfiguredBlockCondition<?, ?>... conditions) {return OR.get().configure(ConditionStreamConfiguration.or(Arrays.asList(conditions), PREDICATE));}

	public static void register() {
		MetaFactories.defineMetaConditions(BLOCK_CONDITIONS, DelegatedBlockCondition::new, ConfiguredBlockCondition.CODEC, PREDICATE);
	}
}
