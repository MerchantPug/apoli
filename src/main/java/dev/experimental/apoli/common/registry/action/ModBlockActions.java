package dev.experimental.apoli.common.registry.action;

import dev.experimental.apoli.api.MetaFactories;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockAction;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockCondition;
import dev.experimental.apoli.common.action.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraftforge.fmllegacy.RegistryObject;
import org.apache.commons.lang3.tuple.Triple;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import static dev.experimental.apoli.common.registry.ApoliRegisters.BLOCK_ACTIONS;

public class ModBlockActions {
	public static final BiConsumer<ConfiguredBlockAction<?, ?>, Triple<Level, BlockPos, Direction>> EXECUTOR = (action, o) -> action.execute(o.getLeft(), o.getMiddle(), o.getRight());
	public static final BiPredicate<ConfiguredBlockCondition<?, ?>, Triple<Level, BlockPos, Direction>> PREDICATE = (condition, triple) -> condition.check(new BlockInWorld(triple.getLeft(), triple.getMiddle(), true));

	public static final RegistryObject<OffsetAction> OFFSET = BLOCK_ACTIONS.register("offset", OffsetAction::new);
	public static final RegistryObject<SetBlockAction> SET_BLOCK = BLOCK_ACTIONS.register("set_block", SetBlockAction::new);
	public static final RegistryObject<AddBlockAction> ADD_BLOCK = BLOCK_ACTIONS.register("add_block", AddBlockAction::new);
	public static final RegistryObject<ExecuteCommandBlockAction> EXECUTE_COMMAND = BLOCK_ACTIONS.register("execute_command", ExecuteCommandBlockAction::new);

	public static void register() {
		MetaFactories.defineMetaActions(BLOCK_ACTIONS, DelegatedBlockAction::new, ConfiguredBlockAction.CODEC, ConfiguredBlockCondition.CODEC, EXECUTOR, PREDICATE);
	}
}
