package dev.experimental.apoli.common.condition.item;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.power.factory.ItemCondition;
import dev.experimental.apoli.common.condition.meta.IDelegatedConditionConfiguration;
import net.minecraft.world.item.ItemStack;

public class DelegatedItemCondition<T extends IDelegatedConditionConfiguration<ItemStack>> extends ItemCondition<T> {
	public DelegatedItemCondition(Codec<T> codec) {
		super(codec);
	}


	@Override
	public boolean check(T configuration, ItemStack stack) {
		return configuration.check(stack);
	}
}
