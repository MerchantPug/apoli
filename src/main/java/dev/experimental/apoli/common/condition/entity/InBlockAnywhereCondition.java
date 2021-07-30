package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.common.condition.configuration.InBlockAnywhereConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.AABB;

public class InBlockAnywhereCondition extends EntityCondition<InBlockAnywhereConfiguration> {

	public InBlockAnywhereCondition() {
		super(InBlockAnywhereConfiguration.CODEC);
	}

	@Override
	public boolean check(InBlockAnywhereConfiguration configuration, LivingEntity entity) {
		int stopAt = configuration.comparison().getOptimalStoppingPoint();
		int count = 0;
		AABB box = entity.getBoundingBox();
		BlockPos blockPos = new BlockPos(box.minX + 0.001D, box.minY + 0.001D, box.minZ + 0.001D);
		BlockPos blockPos2 = new BlockPos(box.maxX - 0.001D, Math.min(box.maxY - 0.001D, entity.level.getHeight()), box.maxZ - 0.001D);
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int i = blockPos.getX(); i <= blockPos2.getX() && count < stopAt; ++i) {
			for (int j = blockPos.getY(); j <= blockPos2.getY() && count < stopAt; ++j) {
				for (int k = blockPos.getZ(); k <= blockPos2.getZ() && count < stopAt; ++k) {
					mutable.set(i, j, k);
					if (configuration.blockCondition().check(new BlockInWorld(entity.level, mutable, false))) {
						count++;
					}
				}
			}
		}
		return configuration.comparison().check(count);
	}
}
