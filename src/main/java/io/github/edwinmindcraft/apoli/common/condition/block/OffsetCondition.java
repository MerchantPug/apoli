package io.github.edwinmindcraft.apoli.common.condition.block;

import io.github.edwinmindcraft.apoli.common.action.configuration.OffsetConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class OffsetCondition extends BlockCondition<OffsetConfiguration<ConfiguredBlockCondition<?, ?>>> {

	public OffsetCondition() {
		super(OffsetConfiguration.codec("condition", ConfiguredBlockCondition.CODEC));
	}

	@Override
	protected boolean check(OffsetConfiguration<ConfiguredBlockCondition<?, ?>> configuration, BlockInWorld block) {
		return configuration.value().check(new BlockInWorld(block.getLevel(), block.getPos().offset(configuration.asBlockPos()), true));
	}
}
