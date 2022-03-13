package io.github.edwinmindcraft.apoli.common.condition.item;

import com.mojang.serialization.Codec;
import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import io.github.edwinmindcraft.apoli.common.action.configuration.ItemHasPowerConfiguration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Arrays;

public class ItemHasPowerCondition extends ItemCondition<ItemHasPowerConfiguration> {
	public ItemHasPowerCondition() {
		super(ItemHasPowerConfiguration.CODEC);
	}

	@Override
	protected boolean check(ItemHasPowerConfiguration configuration, Level level, ItemStack stack) {
		return Arrays.stream(configuration.target()).flatMap(x -> StackPowerUtil.getPowers(stack, x).stream()).anyMatch(x -> configuration.power().equals(x.powerId));
	}
}
