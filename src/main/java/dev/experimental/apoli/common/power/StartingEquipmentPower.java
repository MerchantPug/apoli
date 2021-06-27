package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.StartingInventoryConfiguration;
import io.github.apace100.apoli.Apoli;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

import java.util.Comparator;

public class StartingEquipmentPower extends PowerFactory<StartingInventoryConfiguration> {
	public StartingEquipmentPower() {
		super(StartingInventoryConfiguration.CODEC, false);
	}

	@Override
	protected void onGained(StartingInventoryConfiguration configuration, LivingEntity player) {
		if (player instanceof PlayerEntity ple)
			this.giveStacks(configuration, ple);
	}

	@Override
	protected void onRespawn(StartingInventoryConfiguration configuration, LivingEntity player) {
		if (configuration.recurrent() && player instanceof PlayerEntity ple)
			this.giveStacks(configuration, ple);
	}

	private void giveStacks(StartingInventoryConfiguration configuration, PlayerEntity player) {
		configuration.stacks().getContent().stream().sorted(Comparator.comparingInt(Pair<Integer, ItemStack>::getLeft).reversed()).forEach(x -> {
			Apoli.LOGGER.info("Giving player {} stack: {}", player.getName().asString(), x.getRight().toString());
			int pos = x.getLeft();
			if (pos > 0 && player.getInventory().getStack(pos).isEmpty())
				player.getInventory().setStack(pos, x.getRight().copy());
			else
				player.giveItemStack(x.getRight().copy());
		});
	}
}
