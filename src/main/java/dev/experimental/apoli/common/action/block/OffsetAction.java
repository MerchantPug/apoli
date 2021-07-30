package dev.experimental.apoli.common.action.block;

import dev.experimental.apoli.common.action.configuration.OffsetConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockAction;
import dev.experimental.apoli.api.power.factory.BlockAction;

public class OffsetAction extends BlockAction<OffsetConfiguration<ConfiguredBlockAction<?, ?>>> {

	public OffsetAction() {
		super(OffsetConfiguration.codec("action", ConfiguredBlockAction.CODEC));
	}

	@Override
	public void execute(OffsetConfiguration<ConfiguredBlockAction<?, ?>> configuration, Level world, BlockPos pos, Direction direction) {
		configuration.value().execute(world, pos.offset(configuration.asBlockPos()), direction);
	}
}
