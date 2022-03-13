package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.EntityActionConfiguration;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class EntityActionPower extends PowerFactory<EntityActionConfiguration> {
	/**
	 * Executes all powers for the given factory on the given entity.
	 * @param power The {@link PowerFactory} to execute.
	 * @param entity The entity to execute the powers on.
	 * @return {@code true} if any power exists, {@code false} otherwise.
	 */
	public static boolean execute(EntityActionPower power, Entity entity) {
		List<ConfiguredPower<EntityActionConfiguration, EntityActionPower>> powers = IPowerContainer.getPowers(entity, power);
		powers.forEach(x -> ConfiguredEntityAction.execute(x.getConfiguration().entityAction(), entity));
		return !powers.isEmpty();
	}

	public EntityActionPower() {
		super(EntityActionConfiguration.CODEC);
	}
}
