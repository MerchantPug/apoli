package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.world.entity.Entity;

public interface ITogglePower<T extends IDynamicFeatureConfiguration> extends IActivePower<T> {
	void toggle(ConfiguredPower<T, ?> configuration, Entity entity);
}
