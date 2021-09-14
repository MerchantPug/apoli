package io.github.edwinmindcraft.apoli.common.action.entity;

import com.mojang.math.Vector3f;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.AddVelocityConfiguration;
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
		method.accept(vec.x(), vec.y(), vec.z());
		entity.hurtMarked = true;
	}
}
