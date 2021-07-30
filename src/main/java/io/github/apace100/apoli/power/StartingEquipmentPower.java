package io.github.apace100.apoli.power;

import io.github.apace100.apoli.Apoli;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class StartingEquipmentPower extends Power {

    private final List<ItemStack> itemStacks = new LinkedList<>();
    private final HashMap<Integer, ItemStack> slottedStacks = new HashMap<>();
    private boolean recurrent;

    public StartingEquipmentPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }

    public void setRecurrent(boolean recurrent) {
        this.recurrent = recurrent;
    }

    public void addStack(ItemStack stack) {
        this.itemStacks.add(stack);
    }

    public void addStack(int slot, ItemStack stack) {
        slottedStacks.put(slot, stack);
    }

    @Override
    public void onGained() {
        giveStacks();
    }

    @Override
    public void onRespawn() {
        if(recurrent) {
            giveStacks();
        }
    }

    private void giveStacks() {
        slottedStacks.forEach((slot, stack) -> {
            if(entity instanceof Player) {
                Player player = (Player)entity;
                Inventory inventory = player.getInventory();
                if(inventory.getItem(slot).isEmpty()) {
                    inventory.setItem(slot, stack);
                } else {
                    player.addItem(stack);
                }
            } else {
                entity.spawnAtLocation(stack);
            }
        });
        itemStacks.forEach(is -> {
            ItemStack copy = is.copy();
            if(entity instanceof Player) {
                Player player = (Player)entity;
                player.addItem(copy);
            } else {
                entity.spawnAtLocation(copy);
            }
        });
    }
}
