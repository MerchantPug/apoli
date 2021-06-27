package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.ModifyFallingConfiguration;

//FIXME Forge has a gravity attribute. I need to use that.
public class ModifyFallingPower extends PowerFactory<ModifyFallingConfiguration> {
	public ModifyFallingPower() {
		super(ModifyFallingConfiguration.CODEC);
	}
}
