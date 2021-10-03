package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredFactory;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemAction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class ConfiguredItemAction<C extends IDynamicFeatureConfiguration, F extends ItemAction<C>> extends ConfiguredFactory<C, F> {
	public static final Codec<ConfiguredItemAction<?, ?>> CODEC = ItemAction.CODEC.dispatch(ConfiguredItemAction::getFactory, ItemAction::getCodec);

	public static void execute(@Nullable ConfiguredItemAction<?, ?> action, ItemStack stack) {
		if (action != null) action.execute(stack);
	}

	public ConfiguredItemAction(F factory, C configuration) {
		super(factory, configuration);
	}

	public void execute(ItemStack stack) {
		this.getFactory().execute(this.getConfiguration(), stack);
	}

	@Override
	public String toString() {
		return "CIA:" + this.getFactory().getRegistryName() + "-" + this.getConfiguration();
	}
}
