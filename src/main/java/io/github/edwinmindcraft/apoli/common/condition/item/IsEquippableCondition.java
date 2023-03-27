package io.github.edwinmindcraft.apoli.common.condition.item;


import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class IsEquippableCondition extends ItemCondition<FieldConfiguration<EquipmentSlot>> {
    public IsEquippableCondition() {
        super(FieldConfiguration.codec(SerializableDataTypes.EQUIPMENT_SLOT, "equipment_slot"));
    }

    @Override
    protected boolean check(FieldConfiguration<EquipmentSlot> configuration, @Nullable Level level, ItemStack stack) {
        return Mob.getEquipmentSlotForItem(stack) == configuration.value();
    }
}
