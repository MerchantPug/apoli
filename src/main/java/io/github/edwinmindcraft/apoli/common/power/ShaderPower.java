package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ShaderConfiguration;

public class ShaderPower extends PowerFactory<ShaderConfiguration> {

	public ShaderPower() {
		super(ShaderConfiguration.CODEC);
	}
}
