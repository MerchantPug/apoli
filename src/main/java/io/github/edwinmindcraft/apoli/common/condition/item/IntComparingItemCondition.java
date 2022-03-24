package io.github.edwinmindcraft.apoli.common.condition.item;

import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class IntComparingItemCondition extends ItemCondition<IntegerComparisonConfiguration> {
	private final Function<ItemStack, Integer> function;

	public IntComparingItemCondition(Function<ItemStack, Integer> function) {
		super(IntegerComparisonConfiguration.CODEC);
		this.function = function;
	}

	@Override
	protected boolean check(IntegerComparisonConfiguration configuration, @Nullable Level level, ItemStack stack) {
		Integer apply = this.function.apply(stack);
		return apply != null && configuration.check(apply);
	}
}
