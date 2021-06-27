package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.configuration.NoConfiguration;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import io.github.apace100.apoli.mixin.EntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class SimpleEntityCondition extends EntityCondition<NoConfiguration> {
	public static SimpleEntityCondition of(Predicate<PlayerEntity> predicate) {
		return new SimpleEntityCondition(living -> living instanceof PlayerEntity pe && predicate.test(pe));
	}

	public static boolean isExposedToSky(LivingEntity entity) {
		if (!entity.world.isDay() || ((EntityAccessor) entity).callIsBeingRainedOn())
			return false;
		BlockPos bp = new BlockPos(entity.getX(), (double) Math.round(entity.getY()), entity.getZ());
		if (entity.getVehicle() instanceof BoatEntity) bp = bp.up();
		return entity.world.isSkyVisible(bp);
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
