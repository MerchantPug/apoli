package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.configuration.FloatComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public class FloatComparingEntityCondition extends EntityCondition<FloatComparisonConfiguration> {
	private final Function<Entity, Float> function;

	public FloatComparingEntityCondition(Function<Entity, Float> function) {
		super(FloatComparisonConfiguration.CODEC);
		this.function = function;
	}

	@Override
	public boolean check(FloatComparisonConfiguration configuration, Entity entity) {
		Float apply = this.function.apply(entity);
		return apply != null && configuration.check(apply);
	}
}
