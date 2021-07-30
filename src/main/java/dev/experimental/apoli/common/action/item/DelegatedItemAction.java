package dev.experimental.apoli.common.action.item;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.common.action.meta.IDelegatedActionConfiguration;
import net.minecraft.world.item.ItemStack;
import dev.experimental.apoli.api.power.factory.ItemAction;

public class DelegatedItemAction<T extends IDelegatedActionConfiguration<ItemStack>> extends ItemAction<T> {
	public DelegatedItemAction(Codec<T> codec) {
		super(codec);
	}

	@Override
	public void execute(T configuration, ItemStack stack) {
		configuration.execute(stack);
	}
}
