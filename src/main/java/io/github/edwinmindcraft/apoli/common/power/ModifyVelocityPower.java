package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.IValueModifyingPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyVelocityConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ModifyVelocityPower extends PowerFactory<ModifyVelocityConfiguration> implements IValueModifyingPower<ModifyVelocityConfiguration> {


	public static Vec3 getModifiedVelocity(Entity entity, Vec3 original) {
		return new Vec3(
				IPowerContainer.modify(entity, ApoliPowers.MODIFY_VELOCITY.get(), (float)original.x, p -> p.value().getConfiguration().axes().contains(Direction.Axis.X)),
				IPowerContainer.modify(entity, ApoliPowers.MODIFY_VELOCITY.get(), (float)original.y, p -> p.value().getConfiguration().axes().contains(Direction.Axis.Y)),
				IPowerContainer.modify(entity, ApoliPowers.MODIFY_VELOCITY.get(), (float)original.z, p -> p.value().getConfiguration().axes().contains(Direction.Axis.Z))
		);
	}

	public ModifyVelocityPower() {
		super(ModifyVelocityConfiguration.CODEC);
	}

	@Override
	public List<ConfiguredModifier<?>> getModifiers(ConfiguredPower<ModifyVelocityConfiguration, ?> configuration, Entity player) {
		return configuration.getConfiguration().modifiers().entries();
	}
}

