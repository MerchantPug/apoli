package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.CommandComparisonConfiguration;
import net.minecraft.world.entity.Entity;

import java.util.OptionalInt;

public class CommandCondition extends EntityCondition<CommandComparisonConfiguration> {

	public CommandCondition() {
		super(CommandComparisonConfiguration.CODEC);
	}

	@Override
	public boolean check(CommandComparisonConfiguration configuration, Entity entity) {
		OptionalInt execute = configuration.command().execute(entity);
		return execute.isPresent() && configuration.comparison().check(execute.getAsInt());
	}
}
