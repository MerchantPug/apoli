package io.github.edwinmindcraft.apoli.api.power.configuration.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.apace100.apoli.util.HudRender;

public interface ICooldownPowerConfiguration extends IDynamicFeatureConfiguration {
	int duration();

	HudRender hudRender();
}
