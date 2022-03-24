package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.VehicleActionConfiguration;
import net.minecraft.world.entity.Entity;

public class RidingAction extends EntityAction<VehicleActionConfiguration> {
	public RidingAction() {
		super(VehicleActionConfiguration.CODEC);
	}

	@Override
	public void execute(VehicleActionConfiguration configuration, Entity entity) {
		if (!entity.isPassenger() || (!configuration.biEntityAction().isBound() && !configuration.action().isBound()))
			return;
		Entity vehicle = entity.getVehicle();
		while (vehicle != null) {
			if (ConfiguredBiEntityCondition.check(configuration.biEntityCondition(), entity, vehicle)) {
				ConfiguredEntityAction.execute(configuration.action(), vehicle);
				ConfiguredBiEntityAction.execute(configuration.biEntityAction(), entity, vehicle);
			}
			vehicle = configuration.recursive() ? vehicle.getVehicle() : null; //Lazy mode enabled.
		}
	}
}
