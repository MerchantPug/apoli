package dev.experimental.apoli.api.power.configuration.power;

import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.apoli.util.HudRender;

public interface ICooldownPowerConfiguration extends IDynamicFeatureConfiguration {
	int duration();

	HudRender hudRender();
}
