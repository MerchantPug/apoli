package io.github.apace100.apoli.util;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Comparator;
import java.util.List;

public final class AttributeUtil {

	@Deprecated
	public static void sortModifiers(List<AttributeModifier> modifiers) {
		modifiers.sort(Comparator.comparing(e -> e.getOperation().toValue()));
	}

	/**
	 * @deprecated Use {@link #applyModifiers(List, double)} instead.
	 * That version doesn't have the sorting overhead.
	 */
	@Deprecated
	public static double sortAndApplyModifiers(List<AttributeModifier> modifiers, double baseValue) {
		sortModifiers(modifiers);
		return applyModifiers(modifiers, baseValue);
	}

	public static double applyModifiers(List<AttributeModifier> modifiers, double baseValue) {
		double value = baseValue;
		double multiplier = 1.0F;
		for (AttributeModifier modifier : modifiers) {
			switch (modifier.getOperation()) {
				case ADDITION -> value += modifier.getAmount();
				case MULTIPLY_BASE -> value += modifier.getAmount() * baseValue;
				case MULTIPLY_TOTAL -> multiplier *= (1 + modifier.getAmount());
			}
		}
		return multiplier * value;
	}
}
