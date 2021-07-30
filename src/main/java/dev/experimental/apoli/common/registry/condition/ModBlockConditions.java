package dev.experimental.apoli.common.registry.condition;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.experimental.apoli.api.MetaFactories;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockCondition;
import dev.experimental.apoli.api.power.factory.BlockCondition;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import dev.experimental.apoli.common.condition.block.*;
import io.github.apace100.apoli.Apoli;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class ModBlockConditions {
	public static final BiPredicate<ConfiguredBlockCondition<?, ?>, BlockInWorld> PREDICATE = (config, position) -> config.check(position);

	public static final RegistrySupplier<SimpleBlockCondition> MOVEMENT_BLOCKING = register("movement_blocking", () -> new SimpleBlockCondition(SimpleBlockCondition.MOVEMENT_BLOCKING));
	public static final RegistrySupplier<SimpleBlockCondition> REPLACEABLE_LEGACY = register("replacable", () -> new SimpleBlockCondition(SimpleBlockCondition.REPLACEABLE)); //This one has a typo.
	public static final RegistrySupplier<SimpleBlockCondition> REPLACEABLE = register("replaceable", () -> new SimpleBlockCondition(SimpleBlockCondition.REPLACEABLE)); //This one doesn't have a typo.
	public static final RegistrySupplier<SimpleBlockCondition> LIGHT_BLOCKING = register("light_blocking", () -> new SimpleBlockCondition(SimpleBlockCondition.LIGHT_BLOCKING));
	public static final RegistrySupplier<SimpleBlockCondition> WATER_LOGGABLE = register("water_loggable", () -> new SimpleBlockCondition(SimpleBlockCondition.WATER_LOGGABLE));
	public static final RegistrySupplier<SimpleBlockCondition> EXPOSED_TO_SKY = register("exposed_to_sky", () -> new SimpleBlockCondition(SimpleBlockCondition.EXPOSED_TO_SKY));
	public static final RegistrySupplier<InTagBlockCondition> IN_TAG = register("in_tag", InTagBlockCondition::new);
	public static final RegistrySupplier<FluidBlockCondition> FLUID = register("fluid", FluidBlockCondition::new);
	public static final RegistrySupplier<OffsetCondition> OFFSET = register("offset", OffsetCondition::new);
	public static final RegistrySupplier<AttachableCondition> ATTACHABLE = register("attachable", AttachableCondition::new);
	public static final RegistrySupplier<BlockTypeCondition> BLOCK = register("block", BlockTypeCondition::new);
	public static final RegistrySupplier<AdjacentCondition> ADJACENT = register("adjacent", AdjacentCondition::new);
	public static final RegistrySupplier<LightLevelCondition> LIGHT_LEVEL = register("light_level", LightLevelCondition::new);
	public static final RegistrySupplier<BlockStateCondition> BLOCK_STATE = register("block_state", BlockStateCondition::new);
	public static final RegistrySupplier<HeightCondition> HEIGHT = register("height", HeightCondition::new);

	public static void register() {
		MetaFactories.defineMetaConditions(ApoliRegistries.BLOCK_CONDITION, DelegatedBlockCondition::new, ConfiguredBlockCondition.CODEC, PREDICATE);
	}

	private static <T extends BlockCondition<?>> RegistrySupplier<T> register(String name, Supplier<T> factory) {
		return ApoliRegistries.BLOCK_CONDITION.register(Apoli.identifier(name), factory);
	}
}
