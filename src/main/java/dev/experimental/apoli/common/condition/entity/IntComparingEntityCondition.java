package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.configuration.IntegerComparisonConfiguration;
import dev.experimental.apoli.api.power.factory.EntityCondition;
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
		Integer apply = function.apply(entity);
		return apply != null && configuration.check(apply);
	}
}
