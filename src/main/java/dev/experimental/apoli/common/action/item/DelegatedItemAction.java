package dev.experimental.apoli.common.action.item;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.common.action.meta.IDelegatedActionConfiguration;
import dev.experimental.apoli.api.power.factory.ItemAction;
import net.minecraft.item.ItemStack;

public class DelegatedItemAction<T extends IDelegatedActionConfiguration<ItemStack>> extends ItemAction<T> {
	public DelegatedItemAction(Codec<T> codec) {
		super(codec);
	}

	@Override
	public void execute(T configuration, ItemStack stack) {
		configuration.execute(stack);
	}
}
