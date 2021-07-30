package dev.experimental.apoli.common.condition.item;

import dev.experimental.apoli.api.power.factory.ItemCondition;
import dev.experimental.apoli.common.condition.configuration.EnchantmentConfiguration;
import net.minecraft.world.item.ItemStack;

public class EnchantmentCondition extends ItemCondition<EnchantmentConfiguration> {

	public EnchantmentCondition() {
		super(EnchantmentConfiguration.CODEC);
	}

	@Override
	public boolean check(EnchantmentConfiguration configuration, ItemStack stack) {
		return configuration.applyCheck(stack);
	}
}
