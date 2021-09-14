package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ColorConfiguration;

public class ModelColorPower extends PowerFactory<ColorConfiguration> {
	public ModelColorPower() {
		super(ColorConfiguration.CODEC);
	}
}
