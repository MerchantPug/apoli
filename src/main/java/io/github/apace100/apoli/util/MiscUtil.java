package io.github.apace100.apoli.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public final class MiscUtil {

	public static BlockState getInWallBlockState(LivingEntity playerEntity) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		for (int i = 0; i < 8; ++i) {
			double d = playerEntity.getX() + (double) (((float) (i % 2) - 0.5F) * playerEntity.getBbWidth() * 0.8F);
			double e = playerEntity.getEyeY() + (double) (((float) ((i >> 1) % 2) - 0.5F) * 0.1F);
			double f = playerEntity.getZ() + (double) (((float) ((i >> 2) % 2) - 0.5F) * playerEntity.getBbWidth() * 0.8F);
			mutable.set(d, e, f);
			BlockState blockState = playerEntity.getLevel().getBlockState(mutable);
			if (blockState.getRenderShape() != RenderShape.INVISIBLE && blockState.isViewBlocking(playerEntity.level, mutable)) {
				return blockState;
			}
		}

		return null;
	}

    /*
    public static <T> Predicate<T> combineOr(Predicate<T> a, Predicate<T> b) {
        if(a == null) {
            return b;
        }
        if(b == null) {
            return a;
        }
        return a.or(b);
    }

    public static <T> Predicate<T> combineAnd(Predicate<T> a, Predicate<T> b) {
        if(a == null) {
            return b;
        }
        if(b == null) {
            return a;
        }
        return a.and(b);
    }
     */
}
