package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.world.entity.Entity;

public interface IHudRenderedPower<T extends IDynamicFeatureConfiguration> {

	HudRender getRenderSettings(ConfiguredPower<T, ?> configuration, Entity player);

	float getFill(ConfiguredPower<T, ?> configuration, Entity player);

	boolean shouldRender(ConfiguredPower<T, ?> configuration, Entity player);
}
