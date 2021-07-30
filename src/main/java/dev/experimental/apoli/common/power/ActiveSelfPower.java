package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.power.ActiveCooldownPowerFactory;
import dev.experimental.apoli.common.power.configuration.ActiveSelfConfiguration;
import net.minecraft.world.entity.LivingEntity;

public class ActiveSelfPower extends ActiveCooldownPowerFactory.Simple<ActiveSelfConfiguration> {
	public ActiveSelfPower() {
		super(ActiveSelfConfiguration.CODEC);
	}

	@Override
	protected void execute(ConfiguredPower<ActiveSelfConfiguration, ?> configuration, LivingEntity player) {
		configuration.getConfiguration().action().execute(player);
	}
}
