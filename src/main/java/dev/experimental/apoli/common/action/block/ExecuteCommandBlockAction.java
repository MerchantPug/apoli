package dev.experimental.apoli.common.action.block;

import dev.experimental.apoli.common.action.configuration.CommandConfiguration;
import dev.experimental.apoli.api.power.factory.BlockAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ExecuteCommandBlockAction extends BlockAction<CommandConfiguration> {
	public ExecuteCommandBlockAction() {
		super(CommandConfiguration.CODEC);
	}

	@Override
	public void execute(CommandConfiguration configuration, World world, BlockPos pos, Direction direction) {
		configuration.execute(world, pos);
	}
}
