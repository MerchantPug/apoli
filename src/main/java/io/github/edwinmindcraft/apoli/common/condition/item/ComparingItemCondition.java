package io.github.edwinmindcraft.apoli.common.condition.item;

import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class ComparingItemCondition extends ItemCondition<IntegerComparisonConfiguration> {
	private final ToIntFunction<ItemStack> function;

	public ComparingItemCondition(ToIntFunction<ItemStack> function) {
		super(IntegerComparisonConfiguration.CODEC);
		this.function = function;
	}

	@Override
	public boolean check(IntegerComparisonConfiguration configuration, @Nullable Level level, ItemStack stack) {
		return configuration.check(this.function.applyAsInt(stack));
	}
}
