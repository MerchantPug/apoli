package dev.experimental.apoli.common.condition.item;

import dev.experimental.apoli.api.configuration.NoConfiguration;
import dev.experimental.apoli.api.power.factory.ItemCondition;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class SimpleItemCondition extends ItemCondition<NoConfiguration> {
	private final Predicate<ItemStack> predicate;

	public SimpleItemCondition(Predicate<ItemStack> predicate) {
		super(NoConfiguration.CODEC);
		this.predicate = predicate;
	}

	@Override
	public boolean check(NoConfiguration configuration, ItemStack stack) {
		return this.predicate.test(stack);
	}
}
