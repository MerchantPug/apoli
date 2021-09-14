package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.StartingInventoryConfiguration;
import io.github.apace100.apoli.Apoli;
import java.util.Comparator;

import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class StartingEquipmentPower extends PowerFactory<StartingInventoryConfiguration> {
	public StartingEquipmentPower() {
		super(StartingInventoryConfiguration.CODEC, false);
	}

	@Override
	protected void onGained(StartingInventoryConfiguration configuration, LivingEntity player) {
		if (player instanceof Player ple)
			this.giveStacks(configuration, ple);
	}

	@Override
	protected void onRespawn(StartingInventoryConfiguration configuration, LivingEntity player) {
		if (configuration.recurrent() && player instanceof Player ple)
			this.giveStacks(configuration, ple);
	}

	private void giveStacks(StartingInventoryConfiguration configuration, Player player) {
		configuration.stacks().getContent().stream().sorted(Comparator.comparingInt(Tuple<Integer, ItemStack>::getA).reversed()).forEach(x -> {
			Apoli.LOGGER.info("Giving player {} stack: {}", player.getName().getString(), x.getB().toString());
			int pos = x.getA();
			if (pos > 0 && player.getInventory().getItem(pos).isEmpty())
				player.getInventory().setItem(pos, x.getB().copy());
			else
				player.addItem(x.getB().copy());
		});
	}
}
