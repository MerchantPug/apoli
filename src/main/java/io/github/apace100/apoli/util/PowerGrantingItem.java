package io.github.apace100.apoli.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Forge: This is a capability. Please do not forget to override {@link ICapabilityProvider} in your class.
 */
public interface PowerGrantingItem {
	/**
	 * Returns a list of the powers that this item grants when it is placed in the given slot.
	 *
	 * @param stack The instance of the stack used.
	 * @param slot  The slot this item is currently equipped in.
	 *
	 * @return A {@link Collection} of powers granted by this item.
	 */
	@NotNull
	Collection<StackPowerUtil.StackPower> getPowers(@NotNull ItemStack stack, @NotNull EquipmentSlot slot);
}
