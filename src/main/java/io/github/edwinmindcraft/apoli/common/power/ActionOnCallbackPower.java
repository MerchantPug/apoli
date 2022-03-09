package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ActionOnCallbackConfiguration;
import net.minecraft.world.entity.Entity;

public class ActionOnCallbackPower extends PowerFactory<ActionOnCallbackConfiguration> {
	public ActionOnCallbackPower() {
		super(ActionOnCallbackConfiguration.CODEC);
	}

	@Override
	protected void onGained(ActionOnCallbackConfiguration configuration, Entity player) {
		ConfiguredEntityAction.execute(configuration.entityActionGained(), player);
	}

	@Override
	protected void onLost(ActionOnCallbackConfiguration configuration, Entity player) {
		ConfiguredEntityAction.execute(configuration.entityActionLost(), player);
	}

	@Override
	protected void onAdded(ActionOnCallbackConfiguration configuration, Entity player) {
		ConfiguredEntityAction.execute(configuration.entityActionAdded(), player);
	}

	@Override
	protected void onRemoved(ActionOnCallbackConfiguration configuration, Entity player) {
		ConfiguredEntityAction.execute(configuration.entityActionRemoved(), player);
	}

	@Override
	protected void onRespawn(ActionOnCallbackConfiguration configuration, Entity player) {
		ConfiguredEntityAction.execute(configuration.entityActionRespawned(), player);
	}
}
