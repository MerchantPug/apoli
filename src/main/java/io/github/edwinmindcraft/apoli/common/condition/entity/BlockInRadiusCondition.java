package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.apace100.apoli.util.Shape;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.BlockInRadiusConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class BlockInRadiusCondition extends EntityCondition<BlockInRadiusConfiguration> {

	public BlockInRadiusCondition() {
		super(BlockInRadiusConfiguration.CODEC);
	}

	@Override
	public boolean check(BlockInRadiusConfiguration configuration, LivingEntity entity) {
		int count = 0;
		int stopAt = configuration.comparison().getOptimalStoppingPoint();
		for (BlockPos pos : Shape.getPositions(entity.blockPosition(), configuration.shape(), configuration.radius())) {
			if (configuration.blockCondition().check(new BlockInWorld(entity.level, pos, true)))
				if (++count == stopAt) break;
		}
		return configuration.comparison().check(count);
	}
}
