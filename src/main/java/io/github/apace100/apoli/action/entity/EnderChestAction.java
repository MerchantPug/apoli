package io.github.apace100.apoli.action.entity;

import io.github.edwinmindcraft.apoli.common.action.entity.SimpleEntityAction;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;

public class EnderChestAction extends SimpleEntityAction {
	private static final Component TITLE = Component.translatable("container.enderchest");

	public static void action(Entity entity) {
		if (!(entity instanceof Player player)) return;
		PlayerEnderChestContainer enderChestInventory = player.getEnderChestInventory();

		player.openMenu(new SimpleMenuProvider((syncId, inventory, _player) -> ChestMenu.threeRows(syncId, inventory, enderChestInventory), TITLE));
		player.awardStat(Stats.OPEN_ENDERCHEST);
	}

	public EnderChestAction() {
		super(EnderChestAction::action);
	}
}

