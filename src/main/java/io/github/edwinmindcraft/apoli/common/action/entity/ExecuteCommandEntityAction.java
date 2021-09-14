package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.edwinmindcraft.apoli.common.action.configuration.CommandConfiguration;
import net.minecraft.world.entity.Entity;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;

public class ExecuteCommandEntityAction extends EntityAction<CommandConfiguration> {

	public ExecuteCommandEntityAction() {
		super(CommandConfiguration.CODEC);
	}

	@Override
	public void execute(CommandConfiguration configuration, Entity entity) {
		configuration.execute(entity);
	}
}
