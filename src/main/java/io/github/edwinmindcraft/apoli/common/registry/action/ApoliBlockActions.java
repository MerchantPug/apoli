package io.github.edwinmindcraft.apoli.common.registry.action;

import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockAction;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.action.block.*;
import io.github.edwinmindcraft.apoli.common.action.entity.DelegatedEntityAction;
import io.github.edwinmindcraft.apoli.common.action.meta.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Triple;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.BLOCK_ACTIONS;

public class ApoliBlockActions {
	public static final BiConsumer<ConfiguredBlockAction<?, ?>, Triple<Level, BlockPos, Direction>> EXECUTOR = (action, o) -> action.execute(o.getLeft(), o.getMiddle(), o.getRight());
	public static final BiPredicate<ConfiguredBlockCondition<?, ?>, Triple<Level, BlockPos, Direction>> PREDICATE = (condition, triple) -> condition.check(new BlockInWorld(triple.getLeft(), triple.getMiddle(), true));

	private static <U extends BlockAction<?>> RegistryObject<U> of(String name) {
		return RegistryObject.of(Apoli.identifier(name), ApoliRegistries.BLOCK_ACTION_CLASS, Apoli.MODID);
	}

	public static final RegistryObject<DelegatedBlockAction<StreamConfiguration<ConfiguredBlockAction<?, ?>, Triple<Level, BlockPos, Direction>>>> AND = of("and");
	public static final RegistryObject<DelegatedBlockAction<ChanceConfiguration<ConfiguredBlockAction<?, ?>, Triple<Level, BlockPos, Direction>>>> CHANCE = of("chance");
	public static final RegistryObject<DelegatedBlockAction<IfElseConfiguration<ConfiguredBlockCondition<?, ?>, ConfiguredBlockAction<?, ?>, Triple<Level, BlockPos, Direction>>>> IF_ELSE = of("if_else");
	public static final RegistryObject<DelegatedBlockAction<StreamConfiguration<ConfiguredBlockAction<?, ?>, Triple<Level, BlockPos, Direction>>>> IF_ELSE_LIST = of("if_else_list");
	public static final RegistryObject<DelegatedBlockAction<ChoiceConfiguration<ConfiguredBlockAction<?, ?>, Triple<Level, BlockPos, Direction>>>> CHOICE = of("choice");
	public static final RegistryObject<DelegatedBlockAction<DelayAction<ConfiguredBlockAction<?, ?>, Triple<Level, BlockPos, Direction>>>> DELAY = of("delay");
	public static final RegistryObject<DelegatedBlockAction<NothingConfiguration<Triple<Level, BlockPos, Direction>>>> NOTHING = of("nothing");

	public static final RegistryObject<OffsetAction> OFFSET = BLOCK_ACTIONS.register("offset", OffsetAction::new);
	public static final RegistryObject<SetBlockAction> SET_BLOCK = BLOCK_ACTIONS.register("set_block", SetBlockAction::new);
	public static final RegistryObject<AddBlockAction> ADD_BLOCK = BLOCK_ACTIONS.register("add_block", AddBlockAction::new);
	public static final RegistryObject<ExecuteCommandBlockAction> EXECUTE_COMMAND = BLOCK_ACTIONS.register("execute_command", ExecuteCommandBlockAction::new);

	public static void register() {
		MetaFactories.defineMetaActions(BLOCK_ACTIONS, DelegatedBlockAction::new, ConfiguredBlockAction.CODEC, ConfiguredBlockCondition.CODEC, EXECUTOR, PREDICATE);
	}
}
