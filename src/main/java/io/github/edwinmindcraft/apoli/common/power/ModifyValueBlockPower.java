package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyValueBlockConfiguration;

public class ModifyValueBlockPower extends ValueModifyingPowerFactory<ModifyValueBlockConfiguration> {
	public ModifyValueBlockPower() {
		super(ModifyValueBlockConfiguration.CODEC);
	}
}
