package io.github.edwinmindcraft.apoli.api.power.factory;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.IFactory;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class BiEntityAction<T extends IDynamicFeatureConfiguration> extends ForgeRegistryEntry<BiEntityAction<?>> implements IFactory<T, ConfiguredBiEntityAction<T, ?>, BiEntityAction<T>> {
	public static final Codec<BiEntityAction<?>> CODEC = ApoliRegistries.codec(ApoliRegistries.BIENTITY_ACTION);

	private final Codec<ConfiguredBiEntityAction<T, ?>> codec;

	protected BiEntityAction(Codec<T> codec) {
		this.codec = IFactory.singleCodec(IFactory.asMap(codec), this::configure, ConfiguredBiEntityAction::getConfiguration);
	}

	public Codec<ConfiguredBiEntityAction<T, ?>> getCodec() {
		return this.codec;
	}

	@Override
	public final ConfiguredBiEntityAction<T, ?> configure(T input) {
		return new ConfiguredBiEntityAction<>(() -> this, input);
	}

	public abstract void execute(T configuration, Entity actor, Entity target);
}
