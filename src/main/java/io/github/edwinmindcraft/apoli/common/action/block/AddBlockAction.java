package io.github.edwinmindcraft.apoli.common.action.block;

import io.github.edwinmindcraft.apoli.api.power.factory.BlockAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.BlockConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class AddBlockAction extends BlockAction<BlockConfiguration> {

	public AddBlockAction() {
		super(BlockConfiguration.codec("block"));
	}

	@Override
	public void execute(BlockConfiguration configuration, Level world, BlockPos pos, Direction direction) {
		world.setBlockAndUpdate(pos.relative(direction), configuration.block().defaultBlockState());
	}
}
