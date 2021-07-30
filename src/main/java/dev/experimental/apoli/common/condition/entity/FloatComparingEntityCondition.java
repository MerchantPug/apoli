package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.configuration.FloatComparisonConfiguration;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import java.util.function.Function;
import net.minecraft.world.entity.LivingEntity;

public class FloatComparingEntityCondition extends EntityCondition<FloatComparisonConfiguration> {
	private final Function<LivingEntity, Float> function;

	public FloatComparingEntityCondition(Function<LivingEntity, Float> function) {
		super(FloatComparisonConfiguration.CODEC);
		this.function = function;
	}

	@Override
	public boolean check(FloatComparisonConfiguration configuration, LivingEntity entity) {
		Float apply = function.apply(entity);
		return apply != null && configuration.check(apply);
	}
}
