package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.EntityAction;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class GiveAction extends EntityAction<FieldConfiguration<ItemStack>> {
	public GiveAction() {
		super(FieldConfiguration.codec(SerializableDataTypes.ITEM_STACK, "stack"));
	}

	@Override
	public void execute(FieldConfiguration<ItemStack> configuration, Entity entity) {
		if (!entity.world.isClient()) {
			ItemStack stack = configuration.value().copy();
			if (entity instanceof PlayerEntity player)
				player.getInventory().offerOrDrop(stack);
			else
				entity.world.spawnEntity(new ItemEntity(entity.world, entity.getX(), entity.getY(), entity.getZ(), stack));
		}
	}
}
