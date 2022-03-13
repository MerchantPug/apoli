package io.github.edwinmindcraft.apoli.common.condition.bientity;

import io.github.edwinmindcraft.apoli.api.configuration.DoubleComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityCondition;
import net.minecraft.world.entity.Entity;

import java.util.function.ToDoubleBiFunction;

public class DoubleComparingBiEntityCondition extends BiEntityCondition<DoubleComparisonConfiguration> {
	private final ToDoubleBiFunction<Entity, Entity> function;

	public DoubleComparingBiEntityCondition(ToDoubleBiFunction<Entity, Entity> function) {
		super(DoubleComparisonConfiguration.CODEC);
		this.function = function;
	}

	@Override
	protected boolean check(DoubleComparisonConfiguration configuration, Entity actor, Entity target) {
		double v = this.function.applyAsDouble(actor, target);
		return !Double.isNaN(v) && configuration.check(v);
	}
}
