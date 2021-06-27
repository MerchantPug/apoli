package dev.experimental.apoli.api.power;

import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerFactory;

public interface IInventoryPower<T extends IDynamicFeatureConfiguration> {
	boolean shouldDropOnDeath(ConfiguredPower<T, ?> configuration, LivingEntity player, ItemStack stack);

	boolean shouldDropOnDeath(ConfiguredPower<T, ?> configuration, LivingEntity player);

	Inventory getInventory(ConfiguredPower<T, ?> configuration, LivingEntity player);

	ScreenHandlerFactory getMenuCreator(ConfiguredPower<T, ?> configuration, LivingEntity player);
}
