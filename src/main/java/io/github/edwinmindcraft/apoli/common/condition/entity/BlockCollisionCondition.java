package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.configuration.ApoliCodecs;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BlockCollisionCondition extends EntityCondition<FieldConfiguration<Vec3>> {

	public BlockCollisionCondition() {
		super(FieldConfiguration.codec(ApoliCodecs.vec3d("offset_")));
	}

	@Override
	public boolean check(FieldConfiguration<Vec3> configuration, Entity entity) {
		AABB boundingBox = entity.getBoundingBox();
		boundingBox = boundingBox.move(configuration.value().multiply(boundingBox.getXsize(), boundingBox.getYsize(), boundingBox.getZsize()));
		return entity.level.getBlockCollisions(entity, boundingBox).iterator().hasNext();
	}
}
