package io.github.apace100.apoli.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.common.registry.ApoliLootFunctions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

public class AddPowerLootFunction extends LootItemConditionalFunction {

	private final EquipmentSlot slot;
	private final ResourceLocation powerId;
	private final boolean hidden;
	private final boolean negative;

	private AddPowerLootFunction(LootItemCondition[] conditions, EquipmentSlot slot, ResourceLocation powerId, boolean hidden, boolean negative) {
		super(conditions);
		this.slot = slot;
		this.powerId = powerId;
		this.hidden = hidden;
		this.negative = negative;
	}

	@NotNull
	public LootItemFunctionType getType() {
		return ApoliLootFunctions.ADD_POWER_LOOT_FUNCTION;
	}

	@Override
	@NotNull
	public ItemStack run(@NotNull ItemStack stack, @NotNull LootContext context) {
		StackPowerUtil.addPower(stack, this.slot, this.powerId, this.hidden, this.negative);
		return stack;
	}

	@NotNull
	public static LootItemConditionalFunction.Builder<?> builder(EquipmentSlot slot, ResourceLocation powerId, boolean hidden, boolean negative) {
		return LootItemConditionalFunction.simpleBuilder((conditions) -> new AddPowerLootFunction(conditions, slot, powerId, hidden, negative));
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<AddPowerLootFunction> {
		@Override
		public void serialize(@NotNull JsonObject object, @NotNull AddPowerLootFunction instance, @NotNull JsonSerializationContext context) {
			super.serialize(object, instance, context);
			object.addProperty("slot", instance.slot.getName());
			object.addProperty("power", instance.powerId.toString());
			if (instance.hidden) object.addProperty("hidden", true);
			if (instance.negative) object.addProperty("negative", true);
		}

		@Override
		@NotNull
		public AddPowerLootFunction deserialize(@NotNull JsonObject object, @NotNull JsonDeserializationContext context, LootItemCondition @NotNull [] conditions) {
			EquipmentSlot slot = SerializableDataTypes.EQUIPMENT_SLOT.read(object.get("slot"));
			ResourceLocation powerId = SerializableDataTypes.IDENTIFIER.read(object.get("power"));
			boolean hidden = GsonHelper.getAsBoolean(object, "hidden", false);
			boolean negative = GsonHelper.getAsBoolean(object, "negative", false);
			return new AddPowerLootFunction(conditions, slot, powerId, hidden, negative);
		}
	}
}
