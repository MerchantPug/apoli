package io.github.apace100.apoli.util;

import net.minecraft.core.BlockPos;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public enum Shape {
	CUBE, CHEBYSHEV,
	STAR, MANHATTAN,
	SPHERE, EUCLIDEAN;

	/**
	 * @deprecated Prefer using {@link #forPositions(BlockPos, Shape, int, Consumer, BooleanSupplier)} where possible.
	 */
	@Deprecated
	public static Collection<BlockPos> getPositions(BlockPos center, Shape shape, int radius) {
		Set<BlockPos> positions = new HashSet<>();
		forPositions(center, shape, radius, positions::add, () -> false);
		return positions;
	}

	/**
	 * APOLI FORGE: Reduces memory usage, as well as
	 */
	public static void forPositions(BlockPos center, Shape shape, int radius, Consumer<BlockPos> consumer, BooleanSupplier stopping) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				for (int k = -radius; k <= radius; k++) {
					if (shape == Shape.CUBE || shape == Shape.CHEBYSHEV
						|| (shape == Shape.SPHERE || shape == Shape.EUCLIDEAN)
						   && i * i + j * j + k * k <= radius * radius
						// The radius can't be negative here (the loops aren't even entered in that case)
						// so there's no behavior change from testing that sqrt(i*i + j*j + k*k) <= radius
						|| (Math.abs(i) + Math.abs(j) + Math.abs(k)) <= radius) {
						mutable.set(center.getX() + i, center.getY() + j, center.getZ() + k);
						consumer.accept(mutable);
					}
					if (stopping.getAsBoolean())
						return;
				}
			}
		}
	}

	public static double getDistance(Shape shape, double xDistance, double yDistance, double zDistance) {
		return switch (shape) {
			case SPHERE, EUCLIDEAN -> Math.sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance);
			case STAR, MANHATTAN -> xDistance + yDistance + zDistance;
			case CUBE, CHEBYSHEV -> Math.max(Math.max(xDistance, yDistance), zDistance);
		};
	}
}
