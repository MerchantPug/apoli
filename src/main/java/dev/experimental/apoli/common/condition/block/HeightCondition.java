package dev.experimental.apoli.common.condition.block;

import dev.experimental.apoli.api.configuration.IntegerComparisonConfiguration;
import dev.experimental.apoli.api.power.factory.BlockCondition;
import net.minecraft.block.pattern.CachedBlockPosition;

public class HeightCondition extends BlockCondition<IntegerComparisonConfiguration> {
	public HeightCondition() {
		super(IntegerComparisonConfiguration.CODEC);
	}

	@Override
	protected boolean check(IntegerComparisonConfiguration configuration, CachedBlockPosition block) {
		return configuration.check(block.getBlockPos().getY());
	}
}
