package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.ColorConfiguration;

public class ModelColorPower extends PowerFactory<ColorConfiguration> {
	public ModelColorPower() {
		super(ColorConfiguration.CODEC);
	}
}
