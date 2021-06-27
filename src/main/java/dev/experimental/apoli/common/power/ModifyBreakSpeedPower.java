package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import dev.experimental.apoli.common.power.configuration.ModifyBreakSpeedConfiguration;

public class ModifyBreakSpeedPower extends ValueModifyingPowerFactory<ModifyBreakSpeedConfiguration> {
	public ModifyBreakSpeedPower() {
		super(ModifyBreakSpeedConfiguration.CODEC);
	}
}
