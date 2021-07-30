package dev.experimental.apoli.common.power;

import Key;
import dev.experimental.apoli.api.power.IActivePower;
import dev.experimental.apoli.api.power.IInventoryPower;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.InventoryConfiguration;
import java.util.function.Function;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;

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
	public Container getInventory(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		return this.getData(configuration, player);
	}

	@Override
	public MenuConstructor getMenuCreator(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		return this.handler.apply(this.getData(configuration, player));
	}

	protected SimpleContainer getData(ConfiguredPower<InventoryConfiguration, ?> configuration, LivingEntity player) {
		return configuration.getPowerData(player, () -> new SimpleInventory(this.size));
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
		if (tag instanceof NbtCompound compoundTag) {
			SimpleInventory data = this.getData(configuration, player);
			DefaultedList<ItemStack> stacks = DefaultedList.ofSize(data.size(), ItemStack.EMPTY);
			Inventories.readNbt(compoundTag, stacks);
			for (int i = 0; i < data.size(); i++)
				data.setStack(i, stacks.get(i));
		}
	}
}
