package io.github.apace100.apoli.power;

import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;

public class InventoryPower extends Power implements Active, Container {

    private final int size;
    private final NonNullList<ItemStack> inventory;
    private final TranslatableComponent containerName;
    private final MenuConstructor factory;
    private final boolean shouldDropOnDeath;
    private final Predicate<ItemStack> dropOnDeathFilter;

    public InventoryPower(PowerType<?> type, LivingEntity entity, String containerName, int size, boolean shouldDropOnDeath, Predicate<ItemStack> dropOnDeathFilter) {
        super(type, entity);
        this.size = size;
        this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
        this.containerName = new TranslatableComponent(containerName);
        this.factory = (i, playerInventory, playerEntity) -> {
            return new DispenserMenu(i, playerInventory, this);
        };
        this.shouldDropOnDeath = shouldDropOnDeath;
        this.dropOnDeathFilter = dropOnDeathFilter;
    }

    @Override
    public void onUse() {
        if(!entity.level.isClientSide && entity instanceof Player) {
            Player player = (Player)entity;
            player.openMenu(new SimpleMenuProvider(factory, containerName));
        }
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, inventory);
        return tag;
    }

    @Override
    public void fromTag(Tag tag) {
        ContainerHelper.loadAllItems((CompoundTag)tag, inventory);
    }

    @Override
    public int getContainerSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return inventory.get(slot).split(amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inventory.get(slot);
        setItem(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.set(slot, stack);
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player player) {
        return player == this.entity;
    }

    @Override
    public void clearContent() {
        for(int i = 0; i < size; i++) {
            setItem(i, ItemStack.EMPTY);
        }
    }

    public boolean shouldDropOnDeath() {
        return shouldDropOnDeath;
    }

    public boolean shouldDropOnDeath(ItemStack stack) {
        return shouldDropOnDeath && dropOnDeathFilter.test(stack);
    }

    private Key key;

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public void setKey(Key key) {
        this.key = key;
    }
}
