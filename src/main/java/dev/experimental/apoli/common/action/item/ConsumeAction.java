package dev.experimental.apoli.common.action.item;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.ItemAction;
import net.minecraft.item.ItemStack;

public class ConsumeAction extends ItemAction<FieldConfiguration<Integer>> {

	public ConsumeAction() {super(FieldConfiguration.codec(Codec.INT, "amount", 1));}

	@Override
	public void execute(FieldConfiguration<Integer> configuration, ItemStack stack) {
		stack.decrement(configuration.value());
	}
}
