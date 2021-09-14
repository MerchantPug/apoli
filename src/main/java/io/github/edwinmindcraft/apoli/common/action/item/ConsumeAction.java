package io.github.edwinmindcraft.apoli.common.action.item;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemAction;
import net.minecraft.world.item.ItemStack;

public class ConsumeAction extends ItemAction<FieldConfiguration<Integer>> {

	public ConsumeAction() {super(FieldConfiguration.codec(Codec.INT, "amount", 1));}

	@Override
	public void execute(FieldConfiguration<Integer> configuration, ItemStack stack) {
		stack.shrink(configuration.value());
	}
}
