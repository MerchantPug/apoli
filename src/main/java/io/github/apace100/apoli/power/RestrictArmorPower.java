package io.github.apace100.apoli.power;

import java.util.HashMap;
import java.util.function.Predicate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class RestrictArmorPower extends Power {
    private final HashMap<EquipmentSlot, Predicate<ItemStack>> armorConditions;

    public RestrictArmorPower(PowerType<?> type, LivingEntity entity, HashMap<EquipmentSlot, Predicate<ItemStack>> armorConditions) {
        super(type, entity);
        this.armorConditions = armorConditions;
    }

    @Override
    public void onGained() {
        super.onGained();
        for(EquipmentSlot slot : armorConditions.keySet()) {
            ItemStack equippedItem = entity.getItemBySlot(slot);
            if(!equippedItem.isEmpty()) {
                if(!canEquip(equippedItem, slot)) {
                    // TODO: Prefer putting armor in inv instead of dropping when inv exists
                    entity.spawnAtLocation(equippedItem, entity.getEyeHeight(entity.getPose()));
                    entity.setItemSlot(slot, ItemStack.EMPTY);
                }
            }
        }
    }

    public boolean canEquip(ItemStack itemStack, EquipmentSlot slot) {
        if (armorConditions.get(slot) == null) return true;
        return !armorConditions.get(slot).test(itemStack);
    }
}
