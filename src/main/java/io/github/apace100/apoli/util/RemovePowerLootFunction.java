package io.github.apace100.apoli.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.common.registry.ApoliLootFunctions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

public class RemovePowerLootFunction extends LootItemConditionalFunction {

    private final EquipmentSlot slot;
    private final ResourceLocation powerId;

    private RemovePowerLootFunction(LootItemCondition[] conditions, EquipmentSlot slot, ResourceLocation powerId) {
        super(conditions);
        this.slot = slot;
        this.powerId = powerId;
    }

    @NotNull
    public LootItemFunctionType getType() {
        return ApoliLootFunctions.REMOVE_POWER_LOOT_FUNCTION.get();
    }

    @Override
    @NotNull
    public ItemStack run(@NotNull ItemStack stack, @NotNull LootContext context) {
        StackPowerUtil.removePower(stack, slot, powerId);
        return stack;
    }

    @NotNull
    public static Builder<?> builder(EquipmentSlot slot, ResourceLocation powerId) {
        return LootItemConditionalFunction.simpleBuilder((conditions) -> new RemovePowerLootFunction(conditions, slot, powerId));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RemovePowerLootFunction> {
        @Override
        public void serialize(@NotNull JsonObject jsonObject, @NotNull RemovePowerLootFunction addPowerLootFunction, @NotNull JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, addPowerLootFunction, jsonSerializationContext);
            jsonObject.addProperty("slot", addPowerLootFunction.slot.getName());
            jsonObject.addProperty("power", addPowerLootFunction.powerId.toString());
        }

        @Override
        @NotNull
        public RemovePowerLootFunction deserialize(@NotNull JsonObject jsonObject, @NotNull JsonDeserializationContext jsonDeserializationContext, LootItemCondition @NotNull [] lootConditions) {
            EquipmentSlot slot = SerializableDataTypes.EQUIPMENT_SLOT.read(jsonObject.get("slot"));
            ResourceLocation powerId = SerializableDataTypes.IDENTIFIER.read(jsonObject.get("power"));
            return new RemovePowerLootFunction(lootConditions, slot, powerId);
        }
    }
}