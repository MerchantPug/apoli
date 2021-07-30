package dev.experimental.apoli.api.power.factory;

import com.mojang.serialization.Codec;
import dev.architectury.core.RegistryEntry;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.IFactory;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemAction;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import net.minecraft.world.item.ItemStack;

public abstract class ItemAction<T extends IDynamicFeatureConfiguration> extends RegistryEntry<ItemAction<?>> implements IFactory<T, ConfiguredItemAction<T, ?>, ItemAction<T>> {
	public static final Codec<ItemAction<?>> CODEC = ApoliRegistries.codec(ApoliRegistries.ITEM_ACTION);
	private final Codec<T> codec;

	protected ItemAction(Codec<T> codec) {
		this.codec = codec;
	}

	@Override
	public Codec<T> getCodec() {
		return codec;
	}

	@Override
	public final ConfiguredItemAction<T, ?> configure(T input) {
		return new ConfiguredItemAction<>(this, input);
	}

	public abstract void execute(T configuration, ItemStack stack);
}
