package dev.experimental.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.ConfiguredFactory;
import dev.experimental.apoli.api.power.factory.ItemAction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.world.item.ItemStack;

public final class ConfiguredItemAction<C extends IDynamicFeatureConfiguration, F extends ItemAction<C>> extends ConfiguredFactory<C, F> {
	public static final Codec<ConfiguredItemAction<?, ?>> CODEC = ItemAction.CODEC.dispatch(ConfiguredItemAction::getFactory, Function.identity());

	public static void execute(@Nullable ConfiguredItemAction<?, ?> action, ItemStack stack) {
		if (action != null) action.execute(stack);
	}

	public ConfiguredItemAction(F factory, C configuration) {
		super(factory, configuration);
	}

	public void execute(ItemStack stack) {
		this.getFactory().execute(this.getConfiguration(), stack);
	}
}
