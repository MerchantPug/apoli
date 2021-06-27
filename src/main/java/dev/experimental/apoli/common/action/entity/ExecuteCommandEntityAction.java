package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.common.action.configuration.CommandConfiguration;
import dev.experimental.apoli.api.power.factory.EntityAction;
import net.minecraft.entity.Entity;

public class ExecuteCommandEntityAction extends EntityAction<CommandConfiguration> {

	public ExecuteCommandEntityAction() {
		super(CommandConfiguration.CODEC);
	}

	@Override
	public void execute(CommandConfiguration configuration, Entity entity) {
		configuration.execute(entity);
	}
}
