package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.IInventoryPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.InventoryConfiguration;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;

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
	public void activate(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		if (!player.level.isClientSide() && player instanceof Player ple)
			ple.openMenu(new SimpleMenuProvider(this.getMenuCreator(configuration, player), new TranslatableComponent(configuration.getConfiguration().inventoryName())));
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
	public Container getInventory(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		return this.getData(configuration, player);
	}

	@Override
	public MenuConstructor getMenuCreator(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		return this.handler.apply(this.getData(configuration, player));
	}

	protected SimpleContainer getData(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		return configuration.getPowerData(player, () -> new SimpleContainer(this.size));
	}

	@Override
	public Tag serialize(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		SimpleContainer data = this.getData(configuration, player);
		NonNullList<ItemStack> stacks = NonNullList.withSize(data.getContainerSize(), ItemStack.EMPTY);
		for (int i = 0; i < data.getContainerSize(); i++)
			stacks.set(i, data.getItem(i));
		return ContainerHelper.saveAllItems(new CompoundTag(), stacks);
	}

	@Override
	public void deserialize(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player, Tag tag) {
		if (tag instanceof CompoundTag compoundTag) {
			SimpleContainer data = this.getData(configuration, player);
			NonNullList<ItemStack> stacks = NonNullList.withSize(data.getContainerSize(), ItemStack.EMPTY);
			ContainerHelper.loadAllItems(compoundTag, stacks);
			for (int i = 0; i < data.getContainerSize(); i++)
				data.setItem(i, stacks.get(i));
		}
	}
}
