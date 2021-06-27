package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.IActivePower;
import dev.experimental.apoli.api.power.IInventoryPower;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.InventoryConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;

import java.util.function.Function;

public class InventoryPower extends PowerFactory<InventoryConfiguration> implements IInventoryPower<InventoryConfiguration>, IActivePower<InventoryConfiguration> {

	private final int size;
	private final Function<Inventory, ScreenHandlerFactory> handler;

	public InventoryPower(int size, Function<Inventory, ScreenHandlerFactory> handler) {
		super(InventoryConfiguration.CODEC);
		this.size = size;
		this.handler = handler;
	}

	@Override
	public void activate(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		if (!player.world.isClient && player instanceof PlayerEntity ple)
			ple.openHandledScreen(new SimpleNamedScreenHandlerFactory(this.getMenuCreator(configuration, player), new TranslatableText(configuration.getConfiguration().inventoryName())));
	}

	@Override
	public Key getKey(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		return configuration.getConfiguration().key();
	}

	@Override
	public boolean shouldDropOnDeath(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player, ItemStack stack) {
		return this.shouldDropOnDeath(configuration, player) && ConfiguredItemCondition.check(configuration.getConfiguration().dropFilter(), stack);
	}

	@Override
	public boolean shouldDropOnDeath(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		return configuration.getConfiguration().dropOnDeath();
	}

	@Override
	public Inventory getInventory(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		return this.getData(configuration, player);
	}

	@Override
	public ScreenHandlerFactory getMenuCreator(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		return this.handler.apply(this.getData(configuration, player));
	}

	protected SimpleInventory getData(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		return configuration.getPowerData(player, () -> new SimpleInventory(this.size));
	}

	@Override
	public NbtElement serialize(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		SimpleInventory data = this.getData(configuration, player);
		DefaultedList<ItemStack> stacks = DefaultedList.ofSize(data.size(), ItemStack.EMPTY);
		for (int i = 0; i < data.size(); i++)
			stacks.set(i, data.getStack(i));
		return Inventories.writeNbt(new NbtCompound(), stacks);
	}

	@Override
	public void deserialize(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player, NbtElement tag) {
		if (tag instanceof NbtCompound compoundTag) {
			SimpleInventory data = this.getData(configuration, player);
			DefaultedList<ItemStack> stacks = DefaultedList.ofSize(data.size(), ItemStack.EMPTY);
			Inventories.readNbt(compoundTag, stacks);
			for (int i = 0; i < data.size(); i++)
				data.setStack(i, stacks.get(i));
		}
	}
}
