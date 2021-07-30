package io.github.apace100.apoli.util;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

import static net.minecraft.util.Mth.cos;
import static net.minecraft.util.Mth.sin;

public enum Space {
	WORLD(x -> Quaternion.ONE),
	LOCAL(x -> new Quaternion(x.getXRot(), x.getYRot(), 0, true)),
	LOCAL_HORIZONTAL(x -> new Quaternion(x.getXRot(), 0, 0, true)),
	VELOCITY(x -> rotation(globalForward(), x.getDeltaMovement())),
	VELOCITY_NORMALIZED(x -> rotation(globalForward(), x.getDeltaMovement().normalize())),
	VELOCITY_HORIZONTAL(x -> rotation(globalForward(), x.getDeltaMovement().subtract(0, x.getDeltaMovement().y, 0))),
	VELOCITY_HORIZONTAL_NORMALIZED(x -> rotation(globalForward(), x.getDeltaMovement().subtract(0, x.getDeltaMovement().y, 0).normalize()));

	private static final Vec3 GLOBAL_FORWARD = new Vec3(0, 0, 1);

	private static Vec3 globalForward() { return GLOBAL_FORWARD; }

	private static Quaternion rotation(Vec3 absolute, Vec3 rotated) {
		Vec3 vec3d = absolute.cross(rotated);
		Quaternion quaternion = new Quaternion(
				(float) vec3d.x, (float) vec3d.y, (float) vec3d.z,
				(float) (Math.sqrt(absolute.lengthSqr() + rotated.lengthSqr()) + absolute.dot(rotated))
		);
		quaternion.normalize();
		return quaternion;
	}

	public static void rotateVectorToBase(Vec3 newBase, Vector3f vector) {
		Vec3 v = GLOBAL_FORWARD.cross(newBase).normalize();
		double c = Math.acos(GLOBAL_FORWARD.dot(newBase));
		Quaternion quat = new Quaternion(new Vector3f((float) v.x, (float) v.y, (float) v.z), (float) c, false);
		vector.transform(quat);
	}

	private final Function<Entity, Quaternion> function;

	Space(Function<Entity, Quaternion> function) {
		this.function = function;
	}

	public Quaternion rotation(Entity entity) {
		return this.function.apply(entity);
	}
}
