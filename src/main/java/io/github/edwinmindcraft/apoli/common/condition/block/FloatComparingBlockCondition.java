package io.github.edwinmindcraft.apoli.common.condition.block;

import io.github.edwinmindcraft.apoli.api.configuration.FloatComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import net.minecraft.util.ToFloatFunction;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.function.Function;

public class FloatComparingBlockCondition extends BlockCondition<FloatComparisonConfiguration> {
	private final ToFloatFunction<BlockInWorld> function;

	public FloatComparingBlockCondition(ToFloatFunction<BlockInWorld> function) {
		super(FloatComparisonConfiguration.CODEC);
		this.function = function;
	}

	@Override
	protected boolean check(FloatComparisonConfiguration configuration, BlockInWorld stack) {
		float apply = this.function.apply(stack);
		return !Float.isNaN(apply) && configuration.check(apply);
	}
}
