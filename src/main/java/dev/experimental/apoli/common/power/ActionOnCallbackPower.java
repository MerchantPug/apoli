package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.configuration.ConfiguredEntityAction;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.ActionOnCallbackConfiguration;
import net.minecraft.world.entity.LivingEntity;

public class ActionOnCallbackPower extends PowerFactory<ActionOnCallbackConfiguration> {
	public ActionOnCallbackPower() {
		super(ActionOnCallbackConfiguration.CODEC);
	}

	@Override
	protected void onGained(ActionOnCallbackConfiguration configuration, LivingEntity player) {
		ConfiguredEntityAction.execute(configuration.entityActionGained(), player);
	}

	@Override
	protected void onLost(ActionOnCallbackConfiguration configuration, LivingEntity player) {
		ConfiguredEntityAction.execute(configuration.entityActionLost(), player);
	}

	@Override
	protected void onAdded(ActionOnCallbackConfiguration configuration, LivingEntity player) {
		ConfiguredEntityAction.execute(configuration.entityActionAdded(), player);
	}

	@Override
	protected void onRemoved(ActionOnCallbackConfiguration configuration, LivingEntity player) {
		ConfiguredEntityAction.execute(configuration.entityActionRemoved(), player);
	}

	@Override
	protected void onRespawn(ActionOnCallbackConfiguration configuration, LivingEntity player) {
		ConfiguredEntityAction.execute(configuration.entityActionRespawned(), player);
	}
}
