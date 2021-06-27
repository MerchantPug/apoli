package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.power.factory.EntityAction;
import dev.experimental.apoli.common.action.configuration.AddVelocityConfiguration;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3f;
import org.apache.logging.log4j.util.TriConsumer;

public class AddVelocityAction extends EntityAction<AddVelocityConfiguration> {
	public AddVelocityAction() {
		super(AddVelocityConfiguration.CODEC);
	}

	@Override
	public void execute(AddVelocityConfiguration configuration, Entity entity) {
		Vec3f vec = configuration.getVector();
		TriConsumer<Float, Float, Float> method = configuration.set() ? entity::setVelocity : entity::addVelocity;
		vec.rotate(configuration.space().rotation(entity));
		method.accept(configuration.x(), configuration.y(), configuration.z());
		entity.velocityModified = true;
	}
}
