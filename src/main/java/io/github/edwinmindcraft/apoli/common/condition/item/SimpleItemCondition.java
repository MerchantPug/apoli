package io.github.edwinmindcraft.apoli.common.condition.item;

import io.github.edwinmindcraft.apoli.api.configuration.NoConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;

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
