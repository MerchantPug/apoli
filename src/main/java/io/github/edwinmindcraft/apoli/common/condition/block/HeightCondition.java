package io.github.edwinmindcraft.apoli.common.condition.block;

import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class HeightCondition extends BlockCondition<IntegerComparisonConfiguration> {
	public HeightCondition() {
		super(IntegerComparisonConfiguration.CODEC);
	}

	@Override
	protected boolean check(IntegerComparisonConfiguration configuration, BlockInWorld block) {
		return configuration.check(block.getPos().getY());
	}
}
