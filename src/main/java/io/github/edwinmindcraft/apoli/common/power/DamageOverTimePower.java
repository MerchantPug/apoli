package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.power.VariableIntPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.DamageOverTimeConfiguration;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import java.util.Map;

public class DamageOverTimePower extends VariableIntPowerFactory.Simple<DamageOverTimeConfiguration> {
	public DamageOverTimePower() {
		super(DamageOverTimeConfiguration.CODEC);
		this.ticking(true);
	}

	protected DataContainer getDataContainer(ConfiguredPower<DamageOverTimeConfiguration, ?> configuration, LivingEntity player) {
		return configuration.getPowerData(player, () -> new DataContainer(configuration.getConfiguration().initialValue(), 0));
	}

	@Override
	protected int get(ConfiguredPower<DamageOverTimeConfiguration, ?> configuration, LivingEntity player) {
		return this.getDataContainer(configuration, player).value;
	}

	@Override
	protected void set(ConfiguredPower<DamageOverTimeConfiguration, ?> configuration, LivingEntity player, int value) {
		this.getDataContainer(configuration, player).value = value;
	}

	@Override
	public void tick(ConfiguredPower<DamageOverTimeConfiguration, ?> configuration, LivingEntity player) {
		if (configuration.isActive(player))
			this.doDamage(configuration, player);
		else
			this.resetDamage(configuration, player);
	}

	protected void doDamage(ConfiguredPower<DamageOverTimeConfiguration, ?> configuration, LivingEntity player) {
		DataContainer dataContainer = this.getDataContainer(configuration, player);
		dataContainer.outOfDamageTicks = 0;
		if (this.getValue(configuration, player) <= 0) {
			this.assign(configuration, player, configuration.getConfiguration().interval());
			player.hurt(configuration.getConfiguration().damageSource(), player.level.getDifficulty() == Difficulty.EASY ? configuration.getConfiguration().damageEasy() : configuration.getConfiguration().damage());
		} else {
			this.decrement(configuration, player);
		}
	}

	protected void resetDamage(ConfiguredPower<DamageOverTimeConfiguration, ?> configuration, LivingEntity player) {
		DataContainer dataContainer = this.getDataContainer(configuration, player);
		if (dataContainer.outOfDamageTicks >= 20)
			this.assign(configuration, player, this.getDamageBegin(configuration.getConfiguration(), player));
		else
			dataContainer.outOfDamageTicks++;
	}

	protected int getDamageBegin(DamageOverTimeConfiguration configuration, LivingEntity player) {
        int prot = this.getProtection(configuration, player);
        int delay = (int)(Math.pow(prot * 2, 1.3) * configuration.protectionEffectiveness());
        return configuration.delay() + delay;
	}

	private int getProtection(DamageOverTimeConfiguration configuration, LivingEntity player) {
		if (configuration.protectionEnchantment() == null) {
			return 0;
		} else {
			Map<EquipmentSlot, ItemStack> enchantedItems = configuration.protectionEnchantment().getSlotItems(player);
			Iterable<ItemStack> iterable = enchantedItems.values();
			int i = 0;
			for (ItemStack itemStack : iterable)
				i += EnchantmentHelper.getItemEnchantmentLevel(configuration.protectionEnchantment(), itemStack);
			return i * enchantedItems.size();
		}
	}

	private static final class DataContainer {
		private int value;
		private int outOfDamageTicks;

		private DataContainer(int value, int outOfDamageTicks) {
			this.value = value;
			this.outOfDamageTicks = outOfDamageTicks;
		}
	}
}
