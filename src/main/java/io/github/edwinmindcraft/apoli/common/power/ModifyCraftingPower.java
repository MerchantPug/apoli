package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyCraftingConfiguration;

public class ModifyCraftingPower extends PowerFactory<ModifyCraftingConfiguration> {

	public ModifyCraftingPower() {
		super(ModifyCraftingConfiguration.CODEC);
	}
}
