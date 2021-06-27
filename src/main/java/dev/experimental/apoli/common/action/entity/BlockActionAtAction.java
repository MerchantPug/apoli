package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockAction;
import dev.experimental.apoli.api.power.factory.EntityAction;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Direction;

public class BlockActionAtAction extends EntityAction<FieldConfiguration<ConfiguredBlockAction<?, ?>>> {

	public BlockActionAtAction() {
		super(FieldConfiguration.codec(ConfiguredBlockAction.CODEC, "block_action"));
	}

	@Override
	public void execute(FieldConfiguration<ConfiguredBlockAction<?, ?>> configuration, Entity entity) {
		configuration.value().execute(entity.world, entity.getBlockPos(), Direction.UP);
	}
}
