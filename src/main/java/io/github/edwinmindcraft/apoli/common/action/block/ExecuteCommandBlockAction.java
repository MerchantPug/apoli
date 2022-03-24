package io.github.edwinmindcraft.apoli.common.action.block;

import io.github.edwinmindcraft.apoli.api.power.factory.BlockAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.CommandConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class ExecuteCommandBlockAction extends BlockAction<CommandConfiguration> {
	public ExecuteCommandBlockAction() {
		super(CommandConfiguration.CODEC);
	}

	@Override
	public void execute(CommandConfiguration configuration, Level world, BlockPos pos, Direction direction) {
		configuration.execute(world, pos);
	}
}
