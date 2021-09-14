package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.configuration.FloatComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
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
		Float apply = this.function.apply(entity);
		return apply != null && configuration.check(apply);
	}
}
