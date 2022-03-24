package io.github.edwinmindcraft.apoli.common.condition.item;

import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.PowerCountConfiguration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class PowerCountCondition extends ItemCondition<PowerCountConfiguration> {
	public PowerCountCondition() {
		super(PowerCountConfiguration.CODEC);
	}

	@Override
	protected boolean check(PowerCountConfiguration configuration, @Nullable Level level, ItemStack stack) {
		int count = Arrays.stream(configuration.target()).mapToInt(x -> StackPowerUtil.getPowers(stack, x).size()).sum();
		return configuration.comparison().check(count);
	}
}
