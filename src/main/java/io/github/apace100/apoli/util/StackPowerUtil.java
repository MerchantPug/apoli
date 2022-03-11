package io.github.apace100.apoli.util;

import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.common.registry.ApoliCapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

import java.util.LinkedList;
import java.util.List;

public final class StackPowerUtil {

	public static void addPower(ItemStack stack, EquipmentSlot slot, ResourceLocation powerId) {
		addPower(stack, slot, powerId, false, false);
	}

	public static void addPower(ItemStack stack, EquipmentSlot slot, ResourceLocation powerId, boolean isHidden, boolean isNegative) {
		StackPower stackPower = new StackPower();
		stackPower.slot = slot;
		stackPower.powerId = powerId;
		stackPower.isHidden = isHidden;
		stackPower.isNegative = isNegative;
		addPower(stack, stackPower);
	}

	public static void addPower(ItemStack stack, StackPower stackPower) {
		CompoundTag nbt = stack.getOrCreateTag();
		ListTag list;
		if (nbt.contains("Powers")) {
			Tag elem = nbt.get("Powers");
			if (elem.getId() != Tag.TAG_LIST) {
				Apoli.LOGGER.warn("Can''t add power {} to item stack {}, as it contains conflicting NBT data.", stackPower.powerId, stack);
				return;
			}
			list = (ListTag) elem;
		} else {
			list = new ListTag();
			nbt.put("Powers", list);
		}
		list.add(stackPower.toNbt());
	}

	public static List<StackPower> getPowers(ItemStack stack, EquipmentSlot slot) {
		CompoundTag nbt = stack.getTag();
		List<StackPower> powers = new LinkedList<>();
		LazyOptional<PowerGrantingItem> capability = stack.getCapability(ApoliCapabilities.POWER_GRANTING_ITEM);
		capability.ifPresent(pgi -> powers.addAll(pgi.getPowers(stack, slot)));
		if (!capability.isPresent() && stack.getItem() instanceof PowerGrantingItem pgi) {
			powers.addAll(pgi.getPowers(stack, slot));
			Apoli.LOGGER.warn("Found item granting powers without proper capability support: " + stack);
		}
		if (nbt != null && nbt.contains("Powers")) {
			ListTag list = nbt.getList("Powers", Tag.TAG_COMPOUND);
			for (int i = 0; i < list.size(); i++) {
				StackPower power = StackPower.fromNbt(list.getCompound(i));
				if (power.slot == slot)
					powers.add(power);
			}
		}
		return powers;
	}

	public static class StackPower {
		public EquipmentSlot slot;
		public ResourceLocation powerId;
		public boolean isHidden;
		public boolean isNegative;

		public CompoundTag toNbt() {
			CompoundTag nbt = new CompoundTag();
			nbt.putString("Slot", this.slot.getName());
			nbt.putString("Power", this.powerId.toString());
			nbt.putBoolean("Hidden", this.isHidden);
			nbt.putBoolean("Negative", this.isNegative);
			return nbt;
		}

		public static StackPower fromNbt(CompoundTag nbt) {
			StackPower stackPower = new StackPower();
			stackPower.slot = EquipmentSlot.byName(nbt.getString("Slot"));
			stackPower.powerId = new ResourceLocation(nbt.getString("Power"));
			stackPower.isHidden = nbt.contains("Hidden") && nbt.getBoolean("Hidden");
			stackPower.isNegative = nbt.contains("Negative") && nbt.getBoolean("Negative");
			return stackPower;
		}
	}
}
