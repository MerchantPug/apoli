package dev.experimental.apoli.common.condition.block;

import dev.experimental.apoli.api.power.factory.BlockCondition;
import dev.experimental.apoli.common.condition.configuration.AdjacentConfiguration;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.math.Direction;

import java.util.Arrays;

public class AdjacentCondition extends BlockCondition<AdjacentConfiguration> {
	public AdjacentCondition() {
		super(AdjacentConfiguration.CODEC);
	}

	@Override
	protected boolean check(AdjacentConfiguration configuration, CachedBlockPosition block) {
		int count = Math.toIntExact(Arrays.stream(Direction.values())
				.map(x -> new CachedBlockPosition(block.getWorld(), block.getBlockPos().offset(x), true))
				.filter(configuration.condition()::check).count());
		return configuration.comparison().check(count);
	}
}
