package io.github.edwinmindcraft.apoli.common.condition.item;

import io.github.edwinmindcraft.apoli.api.configuration.FloatComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class FloatComparingItemCondition extends ItemCondition<FloatComparisonConfiguration> {
	private final Function<ItemStack, Float> function;

	public FloatComparingItemCondition(Function<ItemStack, Float> function) {
		super(FloatComparisonConfiguration.CODEC);
		this.function = function;
	}

	@Override
	protected boolean check(FloatComparisonConfiguration configuration, @Nullable Level level, ItemStack stack) {
		Float apply = this.function.apply(stack);
		return apply != null && configuration.check(apply);
	}
}
