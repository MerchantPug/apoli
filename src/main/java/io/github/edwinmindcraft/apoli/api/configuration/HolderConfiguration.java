package io.github.edwinmindcraft.apoli.api.configuration;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import net.minecraft.core.Holder;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public record HolderConfiguration<T>(Holder<T> holder, boolean required) implements IDynamicFeatureConfiguration {

	public static <T> HolderConfiguration<T> of(Holder<T> holder) {
		return new HolderConfiguration<>(holder, false);
	}

	public static <T extends IForgeRegistryEntry<T>> HolderConfiguration<T> defaultCondition(Supplier<IForgeRegistry<T>> registrySupplier) {
		return new HolderConfiguration<>(registrySupplier.get().getHolder(registrySupplier.get().getDefaultKey()).orElseThrow(), false);
	}

	public static <T> Codec<HolderConfiguration<T>> required(MapCodec<Holder<T>> codec) {
		return codec.xmap(x -> new HolderConfiguration<>(x, true), HolderConfiguration::holder).codec();
	}

	public static <T> Codec<HolderConfiguration<T>> optional(MapCodec<Holder<T>> codec) {
		return codec.xmap(x -> new HolderConfiguration<>(x, false), HolderConfiguration::holder).codec();
	}


	@Override
	public @NotNull List<String> getUnbound() {
		if (this.required() && !this.holder().isBound())
			return ImmutableList.of(IDynamicFeatureConfiguration.holderAsString("holder", this.holder()));
		return ImmutableList.of();
	}
}
