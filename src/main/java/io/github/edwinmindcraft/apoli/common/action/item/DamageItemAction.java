package io.github.edwinmindcraft.apoli.common.action.item;

import io.github.edwinmindcraft.apoli.api.power.factory.ItemAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.DamageItemConfiguration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.Mutable;

public class DamageItemAction extends ItemAction<DamageItemConfiguration> {
	public DamageItemAction() {
		super(DamageItemConfiguration.CODEC);
	}

	@Override
	public void execute(DamageItemConfiguration configuration, Level level, Mutable<ItemStack> stack) {
		ItemStack is = stack.getValue();
		if (is.isDamageableItem()) {
			int amount = configuration.amount();
			int i;
			if (amount > 0 && !configuration.ignoreUnbreaking()) {
				i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, is);
				int j = 0;

				for (int k = 0; i > 0 && k < amount; ++k) {
					if (DigDurabilityEnchantment.shouldIgnoreDurabilityDrop(is, i, level.random)) {
						++j;
					}
				}

				amount -= j;
				if (amount <= 0) {
					return;
				}
			}

			i = is.getDamageValue() + amount;
			is.setDamageValue(i);
			if (i >= is.getMaxDamage()) {
				is.shrink(1);
				is.setDamageValue(0);
			}
		}
	}
}
