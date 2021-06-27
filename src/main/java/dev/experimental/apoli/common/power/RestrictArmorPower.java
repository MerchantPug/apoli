package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.RestrictArmorConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.stream.Stream;

public class RestrictArmorPower extends PowerFactory<RestrictArmorConfiguration> {
	public static boolean isForbidden(LivingEntity player, EquipmentSlot slot, ItemStack stack) {
		return Stream.concat(IPowerContainer.getPowers(player, ModPowers.CONDITIONED_RESTRICT_ARMOR.get()).stream(), IPowerContainer.getPowers(player, ModPowers.RESTRICT_ARMOR.get()).stream())
				.anyMatch(x -> !x.getConfiguration().check(slot, stack));
	}

	public RestrictArmorPower() {
		super(RestrictArmorConfiguration.CODEC, false);
	}

	public boolean canEquip(ConfiguredPower<RestrictArmorConfiguration, ?> configuration, ItemStack stack, EquipmentSlot slot) {
		return configuration.getConfiguration().check(slot, stack);
	}

	@Override
	protected void onGained(RestrictArmorConfiguration configuration, LivingEntity player) {
		configuration.dropIllegalItems(player);
	}
}
