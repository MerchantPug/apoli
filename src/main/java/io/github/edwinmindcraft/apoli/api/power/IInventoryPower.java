package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;

public interface IInventoryPower<T extends IDynamicFeatureConfiguration> {
	boolean shouldDropOnDeath(ConfiguredPower<T, ?> configuration, Entity player, ItemStack stack);

	boolean shouldDropOnDeath(ConfiguredPower<T, ?> configuration, Entity player);

	Container getInventory(ConfiguredPower<T, ?> configuration, Entity player);

	MenuConstructor getMenuCreator(ConfiguredPower<T, ?> configuration, Entity player);
}
