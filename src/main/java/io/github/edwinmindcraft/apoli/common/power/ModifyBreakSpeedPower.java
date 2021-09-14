package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyBreakSpeedConfiguration;

public class ModifyBreakSpeedPower extends ValueModifyingPowerFactory<ModifyBreakSpeedConfiguration> {
	public ModifyBreakSpeedPower() {
		super(ModifyBreakSpeedConfiguration.CODEC);
	}
}
