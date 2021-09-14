package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.power.CooldownPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.CooldownConfiguration;

public class CooldownPower extends CooldownPowerFactory.Simple<CooldownConfiguration> {
	public CooldownPower() {
		super(CooldownConfiguration.CODEC);
	}
}
