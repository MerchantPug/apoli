package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyFallingConfiguration;

//FIXME Forge has a gravity attribute. I need to use that.
public class ModifyFallingPower extends PowerFactory<ModifyFallingConfiguration> {
	public ModifyFallingPower() {
		super(ModifyFallingConfiguration.CODEC);
	}
}
