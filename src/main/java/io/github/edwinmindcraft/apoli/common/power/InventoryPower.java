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
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class InventoryPower extends PowerFactory<InventoryConfiguration> implements IInventoryPower<InventoryConfiguration>, IActivePower<InventoryConfiguration> {

	public static void tryDropItemsOnDeath(ConfiguredPower<InventoryConfiguration, InventoryPower> configured, Player player) {
		if (configured.getFactory().shouldDropOnDeath(configured, player)) {
			Container container = configured.getFactory().getInventory(configured, player);
			for (int i = 0; i < container.getContainerSize(); ++i) {
				ItemStack itemStack = container.getItem(i);
				if (configured.getFactory().shouldDropOnDeath(configured, player, itemStack)) {
					if (!itemStack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemStack)) {
						container.removeItemNoUpdate(i);
					} else {
						player.drop(itemStack, true, false);
						container.setItem(i, ItemStack.EMPTY);
					}
				}
			}
		}
	}

	private int size;
	private Function<Container, MenuConstructor> handler;

	public InventoryPower() {
		super(InventoryConfiguration.CODEC);
	}

	public void createHandler(ConfiguredPower<InventoryConfiguration, ?> configuration) {
		if (size > 0 || handler != null) return;
		switch (configuration.getConfiguration().containerType()) {
			case DOUBLE_CHEST:
				this.size = 54;
				this.handler = inventory -> (i, playerInv, invPlayer) -> new ChestMenu(MenuType.GENERIC_9x6, i,
						playerInv, inventory, 6);
				break;
			case CHEST:
				this.size = 27;
				this.handler = inventory -> (i, playerInv, invPlayer) -> new ChestMenu(MenuType.GENERIC_9x3, i,
						playerInv, inventory, 3);
				break;
			case HOPPER:
				this.size = 5;
				this.handler = inventory -> (i, playerInv, invPlayer) -> new HopperMenu(i,
						playerInv, inventory);
				break;
			case DISPENSER, DROPPER:
			default:
				this.size = 9;
				this.handler = inventory -> (i, playerInv, invPlayer) -> new DispenserMenu(i, playerInv, inventory);
				break;
		}
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

	public int getSize() {
		return this.size;
	}

	public void tryDropItemsOnLost(ConfiguredPower<InventoryConfiguration, InventoryPower> configured, Entity entity) {
		if (!(entity instanceof Player player)) return;
		for (int i = 0; i < size; ++i) {
			ItemStack currentItemStack = configured.getFactory().getInventory(configured, player).getItem(i);
			player.getInventory().placeItemBackInInventory(currentItemStack);
		}
	}


	protected SimpleContainer getData(ConfiguredPower<InventoryConfiguration, ?> configuration, IPowerContainer player) {
		createHandler(configuration);
		return configuration.getPowerData(player, () -> new SimpleContainer(this.size));
	}

	protected SimpleContainer getData(ConfiguredPower<InventoryConfiguration, ?> configuration, Entity player) {
		createHandler(configuration);
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

	@Override
	public void onLost(ConfiguredPower<InventoryConfiguration, ?> configuration, Entity entity) {
		if (configuration.getConfiguration().recoverable())
			tryDropItemsOnLost((ConfiguredPower<InventoryConfiguration, InventoryPower>)configuration, entity);
	}

	public enum ContainerType {
		CHEST,
		DOUBLE_CHEST,
		DROPPER,
		DISPENSER,
		HOPPER
	}
}
