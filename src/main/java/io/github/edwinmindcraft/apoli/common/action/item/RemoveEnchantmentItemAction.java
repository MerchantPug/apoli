package io.github.edwinmindcraft.apoli.common.action.item;

import io.github.edwinmindcraft.apoli.api.power.factory.ItemAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.RemoveEnchantmentConfiguration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.Mutable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RemoveEnchantmentItemAction extends ItemAction<RemoveEnchantmentConfiguration> {
	public RemoveEnchantmentItemAction() {
		super(RemoveEnchantmentConfiguration.CODEC);
	}

	@Override
	public void execute(RemoveEnchantmentConfiguration configuration, Level level, Mutable<ItemStack> stack) {
		if (!stack.getValue().hasTag()) return;
		int levels = configuration.levels().isPresent() ? configuration.levels().get() : -1;
		List<Enchantment> enchs = configuration.enchantments().entries();

		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack.getValue());
		if (enchs.size() > 0) {
			for (Enchantment ench : enchs) {
				int newLevel = levels == -1 ? 0 : enchants.get(ench) - levels;
				if (newLevel <= 0)
					enchants.remove(ench);
				else
					enchants.put(ench, newLevel);
			}
		} else {
			Map<Enchantment, Integer> newEnchants = new LinkedHashMap<>();
			for (Enchantment e : enchants.keySet()) {
				int newLevel = levels == -1 ? 0 : enchants.get(e) - levels;
				if (newLevel > 0) {
					newEnchants.put(e, newLevel);
				}
			}
			enchants = newEnchants;
		}
		EnchantmentHelper.setEnchantments(enchants, stack.getValue());
		if (configuration.resetRepairCost() && !stack.getValue().isEnchanted()) {
			stack.getValue().setRepairCost(0);
		}
	}
}
