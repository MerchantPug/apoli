package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GiveAction extends EntityAction<FieldConfiguration<ItemStack>> {
	public GiveAction() {
		super(FieldConfiguration.codec(SerializableDataTypes.ITEM_STACK, "stack"));
	}

	@Override
	public void execute(FieldConfiguration<ItemStack> configuration, Entity entity) {
		if (!entity.level.isClientSide()) {
			ItemStack stack = configuration.value().copy();
			if (entity instanceof Player player)
				player.getInventory().placeItemBackInInventory(stack);
			else
				entity.level.addFreshEntity(new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), stack));
		}
	}
}
