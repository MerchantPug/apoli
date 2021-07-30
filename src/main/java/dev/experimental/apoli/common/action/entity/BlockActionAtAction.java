package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockAction;
import dev.experimental.apoli.api.power.factory.EntityAction;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

public class BlockActionAtAction extends EntityAction<FieldConfiguration<ConfiguredBlockAction<?, ?>>> {

	public BlockActionAtAction() {
		super(FieldConfiguration.codec(ConfiguredBlockAction.CODEC, "block_action"));
	}

	@Override
	public void execute(FieldConfiguration<ConfiguredBlockAction<?, ?>> configuration, Entity entity) {
		configuration.value().execute(entity.level, entity.blockPosition(), Direction.UP);
	}
}
