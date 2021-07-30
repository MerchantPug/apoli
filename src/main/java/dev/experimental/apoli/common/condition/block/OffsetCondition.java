package dev.experimental.apoli.common.condition.block;

import dev.experimental.apoli.common.action.configuration.OffsetConfiguration;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockCondition;
import dev.experimental.apoli.api.power.factory.BlockCondition;

public class OffsetCondition extends BlockCondition<OffsetConfiguration<ConfiguredBlockCondition<?, ?>>> {

	public OffsetCondition() {
		super(OffsetConfiguration.codec("condition", ConfiguredBlockCondition.CODEC));
	}

	@Override
	protected boolean check(OffsetConfiguration<ConfiguredBlockCondition<?, ?>> configuration, BlockInWorld block) {
		return configuration.value().check(new BlockInWorld(block.getLevel(), block.getPos().offset(configuration.asBlockPos()), true));
	}
}
