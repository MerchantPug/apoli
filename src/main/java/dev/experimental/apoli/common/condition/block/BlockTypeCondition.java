package dev.experimental.apoli.common.condition.block;

import dev.experimental.apoli.common.action.configuration.BlockConfiguration;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import dev.experimental.apoli.api.power.factory.BlockCondition;

public class BlockTypeCondition extends BlockCondition<BlockConfiguration> {

	public BlockTypeCondition() {
		super(BlockConfiguration.codec("block"));
	}

	@Override
	protected boolean check(BlockConfiguration configuration, BlockInWorld block) {
		return block.getState().is(configuration.block());
	}
}
