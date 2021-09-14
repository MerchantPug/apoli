package io.github.edwinmindcraft.apoli.api.power.factory;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.IFactory;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ItemAction<T extends IDynamicFeatureConfiguration> extends ForgeRegistryEntry<ItemAction<?>> implements IFactory<T, ConfiguredItemAction<T, ?>, ItemAction<T>> {
	public static final Codec<ItemAction<?>> CODEC = ApoliRegistries.codec(ApoliRegistries.ITEM_ACTION);
	private final Codec<T> codec;

	protected ItemAction(Codec<T> codec) {
		this.codec = codec;
	}

	@Override
	public Codec<T> getCodec() {
		return this.codec;
	}

	@Override
	public final ConfiguredItemAction<T, ?> configure(T input) {
		return new ConfiguredItemAction<>(this, input);
	}

	public abstract void execute(T configuration, ItemStack stack);
}
