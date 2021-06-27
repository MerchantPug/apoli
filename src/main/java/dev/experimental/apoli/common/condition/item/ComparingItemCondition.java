package dev.experimental.apoli.common.condition.item;

import dev.experimental.apoli.api.configuration.IntegerComparisonConfiguration;
import dev.experimental.apoli.api.power.factory.ItemCondition;
import net.minecraft.item.ItemStack;

import java.util.function.ToIntFunction;

public class ComparingItemCondition extends ItemCondition<IntegerComparisonConfiguration> {
	private final ToIntFunction<ItemStack> function;

	public ComparingItemCondition(ToIntFunction<ItemStack> function) {
		super(IntegerComparisonConfiguration.CODEC);
		this.function = function;
	}

	@Override
	public boolean check(IntegerComparisonConfiguration configuration, ItemStack stack) {
		return configuration.check(this.function.applyAsInt(stack));
	}
}
