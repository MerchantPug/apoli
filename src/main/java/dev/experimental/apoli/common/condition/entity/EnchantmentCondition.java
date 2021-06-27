package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.common.condition.configuration.EnchantmentConfiguration;
import net.minecraft.entity.LivingEntity;

public class EnchantmentCondition extends EntityCondition<EnchantmentConfiguration> {

	public EnchantmentCondition() {
		super(EnchantmentConfiguration.CODEC);
	}

	@Override
	public boolean check(EnchantmentConfiguration configuration, LivingEntity entity) {
		return configuration.enchantment() != null && configuration.applyCheck(configuration.enchantment().getEquipment(entity).values());
	}
}
