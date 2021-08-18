package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.configuration.NoConfiguration;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import io.github.apace100.apoli.mixin.EntityAccessor;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;

public class SimpleEntityCondition extends EntityCondition<NoConfiguration> {
	public static SimpleEntityCondition of(Predicate<Player> predicate) {
		return new SimpleEntityCondition(living -> living instanceof Player pe && predicate.test(pe));
	}

	public static boolean isExposedToSky(LivingEntity entity) {
		if (!entity.level.isDay() || ((EntityAccessor) entity).callIsBeingRainedOn())
			return false;
		BlockPos bp = new BlockPos(entity.getX(), (double) Math.round(entity.getY()), entity.getZ());
		if (entity.getVehicle() instanceof Boat) bp = bp.above();
		return entity.level.canSeeSky(bp);
	}

	private final Predicate<LivingEntity> predicate;

	public SimpleEntityCondition(Predicate<LivingEntity> predicate) {
		super(NoConfiguration.CODEC);
		this.predicate = predicate;
	}

	@Override
	public boolean check(NoConfiguration configuration, LivingEntity entity) {
		return this.predicate.test(entity);
	}
}
