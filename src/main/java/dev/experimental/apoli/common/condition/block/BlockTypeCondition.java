package dev.experimental.apoli.common.condition.block;

import dev.experimental.apoli.common.action.configuration.BlockConfiguration;
import dev.experimental.apoli.api.power.factory.BlockCondition;
import net.minecraft.block.pattern.CachedBlockPosition;

public class BlockTypeCondition extends BlockCondition<BlockConfiguration> {

	public BlockTypeCondition() {
		super(BlockConfiguration.codec("block"));
	}

	@Override
	protected boolean check(BlockConfiguration configuration, CachedBlockPosition block) {
		return block.getBlockState().isOf(configuration.block());
	}
}
