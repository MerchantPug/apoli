package io.github.edwinmindcraft.apoli.common.action.block;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockAction;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.OffsetConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class OffsetAction extends BlockAction<OffsetConfiguration<ConfiguredBlockAction<?, ?>>> {

	public OffsetAction() {
		super(OffsetConfiguration.codec(ConfiguredBlockAction.required("action")));
	}

	@Override
	public void execute(OffsetConfiguration<ConfiguredBlockAction<?, ?>> configuration, Level world, BlockPos pos, Direction direction) {
		ConfiguredBlockAction.execute(configuration.value(), world, pos.offset(configuration.asBlockPos()), direction);
	}
}
