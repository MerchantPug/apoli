package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.configuration.NoConfiguration;
import dev.experimental.apoli.api.power.factory.PowerFactory;

public class DummyPower extends PowerFactory<NoConfiguration> {
	public DummyPower() {
		super(NoConfiguration.CODEC);
	}
}
