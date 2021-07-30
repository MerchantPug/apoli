package dev.experimental.apoli.common.condition.block;

import dev.experimental.apoli.api.power.factory.BlockCondition;
import dev.experimental.apoli.common.condition.configuration.AdjacentConfiguration;
import java.util.Arrays;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class AdjacentCondition extends BlockCondition<AdjacentConfiguration> {
	public AdjacentCondition() {
		super(AdjacentConfiguration.CODEC);
	}

	@Override
	protected boolean check(AdjacentConfiguration configuration, BlockInWorld block) {
		int count = Math.toIntExact(Arrays.stream(Direction.values())
				.map(x -> new BlockInWorld(block.getLevel(), block.getPos().relative(x), true))
				.filter(configuration.condition()::check).count());
		return configuration.comparison().check(count);
	}
}
