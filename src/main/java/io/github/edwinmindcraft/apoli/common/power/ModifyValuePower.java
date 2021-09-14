package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ValueModifyingPowerConfiguration;

public class ModifyValuePower extends ValueModifyingPowerFactory<ValueModifyingPowerConfiguration> {
	public ModifyValuePower() {
		super(ValueModifyingPowerConfiguration.CODEC);
	}
}
