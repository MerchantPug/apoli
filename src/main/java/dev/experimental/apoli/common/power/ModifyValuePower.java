package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import dev.experimental.apoli.common.power.configuration.ValueModifyingPowerConfiguration;

public class ModifyValuePower extends ValueModifyingPowerFactory<ValueModifyingPowerConfiguration> {
	public ModifyValuePower() {
		super(ValueModifyingPowerConfiguration.CODEC);
	}
}
