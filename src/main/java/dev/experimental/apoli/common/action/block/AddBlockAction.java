package dev.experimental.apoli.common.action.block;

import dev.experimental.apoli.api.power.factory.BlockAction;
import dev.experimental.apoli.common.action.configuration.BlockConfiguration;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AddBlockAction extends BlockAction<BlockConfiguration> {

	public AddBlockAction() {
		super(BlockConfiguration.codec("block"));
	}

	@Override
	public void execute(BlockConfiguration configuration, World world, BlockPos pos, Direction direction) {
		world.setBlockState(pos.offset(direction), configuration.block().getDefaultState());
	}
}
