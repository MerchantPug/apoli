package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.apace100.apoli.mixin.EntityAccessor;
import io.github.edwinmindcraft.apoli.api.configuration.NoConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.function.Predicate;

public class SimpleEntityCondition extends EntityCondition<NoConfiguration> {
	public static SimpleEntityCondition of(Predicate<Player> predicate) {
		return new SimpleEntityCondition(living -> living instanceof Player pe && predicate.test(pe));
	}

	public static boolean isExposedToSky(Entity entity) {
		if (!entity.level.isDay() || ((EntityAccessor) entity).callIsBeingRainedOn())
			return false;
		BlockPos bp = new BlockPos(entity.getX(), (double) Math.round(entity.getY()), entity.getZ());
		if (entity.getVehicle() instanceof Boat) bp = bp.above();
		return entity.level.canSeeSky(bp);
	}

	private final Predicate<Entity> predicate;

	public SimpleEntityCondition(Predicate<Entity> predicate) {
		super(NoConfiguration.CODEC);
		this.predicate = predicate;
	}

	@Override
	public boolean check(NoConfiguration configuration, Entity entity) {
		return this.predicate.test(entity);
	}
}
