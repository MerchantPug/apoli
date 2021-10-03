package io.github.edwinmindcraft.apoli.api.power.factory;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.IFactory;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class EntityAction<T extends IDynamicFeatureConfiguration> extends ForgeRegistryEntry<EntityAction<?>> implements IFactory<T, ConfiguredEntityAction<T, ?>, EntityAction<T>> {
	public static final Codec<EntityAction<?>> CODEC = ApoliRegistries.codec(ApoliRegistries.ENTITY_ACTION);

	private final Codec<ConfiguredEntityAction<T, ?>> codec;

	protected EntityAction(Codec<T> codec) {
		this.codec = IFactory.singleCodec(IFactory.asMap(codec), this::configure, ConfiguredEntityAction::getConfiguration);
	}

	public Codec<ConfiguredEntityAction<T, ?>> getCodec() {
		return this.codec;
	}

	@Override
	public final ConfiguredEntityAction<T, ?> configure(T input) {
		return new ConfiguredEntityAction<>(this, input);
	}

	public abstract void execute(T configuration, Entity entity);
}
