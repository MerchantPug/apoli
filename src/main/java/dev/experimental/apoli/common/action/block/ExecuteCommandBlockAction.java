package dev.experimental.apoli.common.action.block;

import dev.experimental.apoli.common.action.configuration.CommandConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import dev.experimental.apoli.api.power.factory.BlockAction;

public class ExecuteCommandBlockAction extends BlockAction<CommandConfiguration> {
	public ExecuteCommandBlockAction() {
		super(CommandConfiguration.CODEC);
	}

	@Override
	public void execute(CommandConfiguration configuration, Level world, BlockPos pos, Direction direction) {
		configuration.execute(world, pos);
	}
}
