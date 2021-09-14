package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.configuration.NoConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;

public class DummyPower extends PowerFactory<NoConfiguration> {
	public DummyPower() {
		super(NoConfiguration.CODEC);
	}
}
