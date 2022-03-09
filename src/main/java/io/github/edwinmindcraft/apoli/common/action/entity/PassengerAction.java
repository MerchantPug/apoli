package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.VehicleActionConfiguration;
import net.minecraft.world.entity.Entity;

public class PassengerAction extends EntityAction<VehicleActionConfiguration> {
	public PassengerAction() {
		super(VehicleActionConfiguration.CODEC);
	}

	@Override
	public void execute(VehicleActionConfiguration configuration, Entity entity) {
		if (!entity.isVehicle() || (configuration.biEntityAction() == null && configuration.action() == null))
			return;
		Iterable<Entity> passengers = configuration.recursive() ? entity.getIndirectPassengers() : entity.getPassengers();
		for (Entity passenger : passengers) {
			if (ConfiguredBiEntityCondition.check(configuration.biEntityCondition(), passenger, entity)) {
				ConfiguredEntityAction.execute(configuration.action(), passenger);
				ConfiguredBiEntityAction.execute(configuration.biEntityAction(), passenger, entity);
			}
		}
	}
}
