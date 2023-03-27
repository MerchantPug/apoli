package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.world.entity.Entity;

import java.util.List;

public interface IValueModifyingPower<T extends IDynamicFeatureConfiguration> {
	List<ConfiguredModifier<?>> getModifiers(ConfiguredPower<T, ?> configuration, Entity player);
}
