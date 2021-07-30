package dev.experimental.apoli.common.action.entity;

import com.mojang.math.Vector3f;
import dev.experimental.apoli.api.power.factory.EntityAction;
import dev.experimental.apoli.common.action.configuration.AddVelocityConfiguration;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.util.TriConsumer;

public class AddVelocityAction extends EntityAction<AddVelocityConfiguration> {
	public AddVelocityAction() {
		super(AddVelocityConfiguration.CODEC);
	}

	@Override
	public void execute(AddVelocityConfiguration configuration, Entity entity) {
		Vector3f vec = configuration.getVector();
		TriConsumer<Float, Float, Float> method = configuration.set() ? entity::setDeltaMovement : entity::push;
		vec.transform(configuration.space().rotation(entity));
		method.accept(configuration.x(), configuration.y(), configuration.z());
		entity.hurtMarked = true;
	}
}
