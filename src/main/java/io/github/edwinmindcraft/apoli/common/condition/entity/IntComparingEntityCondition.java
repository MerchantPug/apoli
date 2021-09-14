package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import java.util.function.Function;

import net.minecraft.world.entity.LivingEntity;

public class IntComparingEntityCondition extends EntityCondition<IntegerComparisonConfiguration> {
	private final Function<LivingEntity, Integer> function;

	public IntComparingEntityCondition(Function<LivingEntity, Integer> function) {
		super(IntegerComparisonConfiguration.CODEC);
		this.function = function;
	}

	@Override
	public boolean check(IntegerComparisonConfiguration configuration, LivingEntity entity) {
		Integer apply = this.function.apply(entity);
		return apply != null && configuration.check(apply);
	}
}
