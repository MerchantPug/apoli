package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

public class IntComparingEntityCondition extends EntityCondition<IntegerComparisonConfiguration> {
	private final Function<Entity, Integer> function;

	public IntComparingEntityCondition(Function<Entity, Integer> function) {
		super(IntegerComparisonConfiguration.CODEC);
		this.function = function;
	}

	@Override
	public boolean check(IntegerComparisonConfiguration configuration, Entity entity) {
		Integer apply = this.function.apply(entity);
		return apply != null && configuration.check(apply);
	}
}
