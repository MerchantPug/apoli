package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.factory.power.CooldownPowerFactory;
import dev.experimental.apoli.common.power.configuration.CooldownConfiguration;

public class CooldownPower extends CooldownPowerFactory.Simple<CooldownConfiguration> {
	public CooldownPower() {
		super(CooldownConfiguration.CODEC);
	}
}
