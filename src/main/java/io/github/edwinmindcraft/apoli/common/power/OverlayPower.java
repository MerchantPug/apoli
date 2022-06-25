package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.OverlayConfiguration;

public class OverlayPower extends PowerFactory<OverlayConfiguration> {
	public OverlayPower() {
		super(OverlayConfiguration.CODEC);
	}
}
