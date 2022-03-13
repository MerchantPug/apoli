package io.github.edwinmindcraft.apoli.api;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.Mutable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record VariableAccess<T>(Supplier<T> reader, Consumer<T> writer) implements Mutable<T>{
	public static Mutable<ItemStack> hand(LivingEntity living, InteractionHand hand) {
		return new VariableAccess<>(() -> living.getItemInHand(hand), stack -> living.setItemInHand(hand, stack));
	}

	public static Mutable<ItemStack> slot(LivingEntity living, EquipmentSlot slot) {
		return new VariableAccess<>(() -> living.getItemBySlot(slot), x -> living.setItemSlot(slot, x));
	}

	public static Mutable<ItemStack> slot(Slot slot) {
		return new VariableAccess<>(slot::getItem, slot::set);
	}

	public static Mutable<ItemStack> slot(SlotAccess slot) {
		return new VariableAccess<>(slot::get, slot::set);
	}

	@Override
	public T getValue() {
		return this.reader().get();
	}

	@Override
	public void setValue(T value) {
		this.writer().accept(value);
	}
}
