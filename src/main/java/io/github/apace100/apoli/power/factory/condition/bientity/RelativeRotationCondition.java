package io.github.apace100.apoli.power.factory.condition.bientity;

import io.github.apace100.apoli.condition.configuration.RelativeRotationConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityCondition;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.function.Function;

public class RelativeRotationCondition extends BiEntityCondition<RelativeRotationConfiguration> {


	public RelativeRotationCondition() {
		super(RelativeRotationConfiguration.CODEC);
	}

	@Override
	protected boolean check(RelativeRotationConfiguration configuration, Entity actor, Entity target) {
		Vec3 vec0 = configuration.actorRotation().getRotation(actor);
		Vec3 vec1 = configuration.targetRotation().getRotation(target);
		EnumSet<Direction.Axis> axes = configuration.axes();
		vec0 = reduceAxes(vec0, axes);
		vec1 = reduceAxes(vec1, axes);
		double angle = getAngleBetween(vec0, vec1);
		return configuration.comparison().check(angle);
	}

	private static double getAngleBetween(Vec3 a, Vec3 b) {
		double dot = a.dot(b);
		return dot / (a.length() * b.length());
	}

	private static Vec3 reduceAxes(Vec3 vector, EnumSet<Direction.Axis> axesToKeep) {
		return new Vec3(
				axesToKeep.contains(Direction.Axis.X) ? vector.x() : 0,
				axesToKeep.contains(Direction.Axis.Y) ? vector.y() : 0,
				axesToKeep.contains(Direction.Axis.Z) ? vector.z() : 0
		);
	}

	private static Vec3 getRotationVector(float pitch, float yaw) {
		float f = pitch * ((float) Math.PI / 180);
		float g = -yaw * ((float) Math.PI / 180);
		float h = Mth.cos(g);
		float i = Mth.sin(g);
		float j = Mth.cos(f);
		float k = Mth.sin(f);
		return new Vec3(i * j, -k, h * j);
	}

	public enum RotationType {
		HEAD(e -> e.getViewVector(1.0F)), BODY(e -> {
			if (e instanceof LivingEntity l) {
				return getRotationVector(0F, l.yBodyRot);
			} else {
				return e.getViewVector(1.0F);
			}
		});

		private final Function<Entity, Vec3> function;

		RotationType(Function<Entity, Vec3> function) {
			this.function = function;
		}

		public Vec3 getRotation(Entity entity) {
			return this.function.apply(entity);
		}
	}
}