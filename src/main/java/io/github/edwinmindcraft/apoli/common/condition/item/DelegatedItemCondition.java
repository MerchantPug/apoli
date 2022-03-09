package io.github.edwinmindcraft.apoli.common.condition.item;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import io.github.edwinmindcraft.apoli.common.condition.meta.IDelegatedConditionConfiguration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DelegatedItemCondition<T extends IDelegatedConditionConfiguration<Pair<Level, ItemStack>>> extends ItemCondition<T> {
	public DelegatedItemCondition(Codec<T> codec) {
		super(codec);
	}


	@Override
	public boolean check(T configuration, Level level, ItemStack stack) {
		return configuration.check(Pair.of(level, stack));
	}
}
