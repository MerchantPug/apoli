package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.world.entity.LivingEntity;

public interface IHudRenderedPower<T extends IDynamicFeatureConfiguration> {

	HudRender getRenderSettings(ConfiguredPower<T, ?> configuration, LivingEntity player);

	float getFill(ConfiguredPower<T, ?> configuration, LivingEntity player);

	boolean shouldRender(ConfiguredPower<T, ?> configuration, LivingEntity player);
}
