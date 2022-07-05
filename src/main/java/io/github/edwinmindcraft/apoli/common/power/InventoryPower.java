package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.IInventoryPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.InventoryConfiguration;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class InventoryPower extends PowerFactory<InventoryConfiguration> implements IInventoryPower<InventoryConfiguration>, IActivePower<InventoryConfiguration> {

	private final int size;
	private final Function<Container, MenuConstructor> handler;

	public InventoryPower(int size, Function<Container, MenuConstructor> handler) {
		super(InventoryConfiguration.CODEC);
		this.size = size;
		this.handler = handler;
	}

	@Override
	public void activate(ConfiguredPower<InventoryConfiguration, ?> configuration, Entity player) {
		if (!player.level.isClientSide() && player instanceof Player ple && configuration.isActive(player))
			ple.openMenu(new SimpleMenuProvider(this.getMenuCreator(configuration, player), Component.translatable(configuration.getConfiguration().inventoryName())));
	}

	@Override
	public Key getKey(ConfiguredPower<InventoryConfiguration, ?> configuration, @Nullable Entity player) {
		return configuration.getConfiguration().key();
	}

	@Override
	public boolean shouldDropOnDeath(ConfiguredPower<InventoryConfiguration, ?> configuration, Entity player, ItemStack stack) {
		return this.shouldDropOnDeath(configuration, player) && ConfiguredItemCondition.check(configuration.getConfiguration().dropFilter(), player.level, stack);
	}

	@Override
	public boolean shouldDropOnDeath(ConfiguredPower<InventoryConfiguration, ?> configuration, Entity player) {
		return configuration.getConfiguration().dropOnDeath();
	}

	@Override
	public Container getInventory(ConfiguredPower<InventoryConfiguration, ?> configuration, Entity player) {
		return this.getData(configuration, player);
	}

	@Override
	public MenuConstructor getMenuCreator(ConfiguredPower<InventoryConfiguration, ?> configuration, Entity player) {
		return this.handler.apply(this.getData(configuration, player));
	}

	protected SimpleContainer getData(ConfiguredPower<InventoryConfiguration, ?> configuration, IPowerContainer player) {
		return configuration.getPowerData(player, () -> new SimpleContainer(this.size));
	}

	protected SimpleContainer getData(ConfiguredPower<InventoryConfiguration, ?> configuration, Entity player) {
		return configuration.getPowerData(player, () -> new SimpleContainer(this.size));
	}

	@Override
	public void serialize(ConfiguredPower<InventoryConfiguration, ?> configuration, IPowerContainer container, CompoundTag tag) {
		SimpleContainer data = this.getData(configuration, container);
		NonNullList<ItemStack> stacks = NonNullList.withSize(data.getContainerSize(), ItemStack.EMPTY);
		for (int i = 0; i < data.getContainerSize(); i++)
			stacks.set(i, data.getItem(i));
		ContainerHelper.saveAllItems(tag, stacks);
	}

	@Override
	public void deserialize(ConfiguredPower<InventoryConfiguration, ?> configuration, IPowerContainer container, CompoundTag tag) {
		SimpleContainer data = this.getData(configuration, container);
		NonNullList<ItemStack> stacks = NonNullList.withSize(data.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, stacks);
		for (int i = 0; i < data.getContainerSize(); i++)
			data.setItem(i, stacks.get(i));
	}
}
