package io.github.edwinmindcraft.apoli.common.util;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.common.power.RestrictArmorPower;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CoreUtils {
	/**
	 * Checks armor equipment conditions.
	 *
	 * @param living The entity to check the conditions for.
	 * @param slot   The slot to check the item against.
	 * @param stack  The stack that is being placed.
	 *
	 * @return {@code true} if there exists a {@link ModPowers#RESTRICT_ARMOR} or {@link ModPowers#CONDITIONED_RESTRICT_ARMOR} that would prevent armor from being
	 * equipped, or if the item is an {@link Items#ELYTRA} and the player has {@link ModPowers#ELYTRA_FLIGHT}
	 */
	public static boolean isItemForbidden(LivingEntity living, EquipmentSlot slot, ItemStack stack) {
		return RestrictArmorPower.isForbidden(living, slot, stack) ||
			   (stack.is(Items.ELYTRA) && IPowerContainer.hasPower(living, ModPowers.ELYTRA_FLIGHT.get()));
	}
}
