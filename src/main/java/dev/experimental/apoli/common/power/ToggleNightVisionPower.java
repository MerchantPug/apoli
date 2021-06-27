package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.INightVisionPower;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.power.TogglePowerFactory;
import dev.experimental.apoli.common.power.configuration.ToggleNightVisionConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ToggleNightVisionPower extends TogglePowerFactory.Simple<ToggleNightVisionConfiguration> implements INightVisionPower<ToggleNightVisionConfiguration> {

	public ToggleNightVisionPower() {
		super(ToggleNightVisionConfiguration.CODEC);
	}

	@Override
	public float getStrength(ConfiguredPower<ToggleNightVisionConfiguration, ?> configuration, LivingEntity player) {
		return configuration.getConfiguration().strength();
	}
}
