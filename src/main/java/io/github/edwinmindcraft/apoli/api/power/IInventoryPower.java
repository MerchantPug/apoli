package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;

public interface IInventoryPower<T extends IDynamicFeatureConfiguration> {
	boolean shouldDropOnDeath(ConfiguredPower<T, ?> configuration, LivingEntity player, ItemStack stack);

	boolean shouldDropOnDeath(ConfiguredPower<T, ?> configuration, LivingEntity player);

	Container getInventory(ConfiguredPower<T, ?> configuration, LivingEntity player);

	MenuConstructor getMenuCreator(ConfiguredPower<T, ?> configuration, LivingEntity player);
}
