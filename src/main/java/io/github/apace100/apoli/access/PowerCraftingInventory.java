package io.github.apace100.apoli.access;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;

public interface PowerCraftingInventory {

	void setPower(ConfiguredPower<?, ?> power);

	ConfiguredPower<?, ?> getPower();
}
