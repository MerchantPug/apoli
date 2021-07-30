package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.common.condition.configuration.CommandComparisonConfiguration;
import java.util.OptionalInt;
import net.minecraft.world.entity.LivingEntity;

public class CommandCondition extends EntityCondition<CommandComparisonConfiguration> {

	public CommandCondition() {
		super(CommandComparisonConfiguration.CODEC);
	}

	@Override
	public boolean check(CommandComparisonConfiguration configuration, LivingEntity entity) {
		OptionalInt execute = configuration.command().execute(entity);
		return execute.isPresent() && configuration.comparison().check(execute.getAsInt());
	}
}
