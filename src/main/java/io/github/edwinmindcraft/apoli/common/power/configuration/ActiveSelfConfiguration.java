package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.HudRender;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IActiveCooldownPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

public record ActiveSelfConfiguration(int duration, HudRender hudRender,
									  ConfiguredEntityAction<?, ?> action,
									  IActivePower.Key key) implements IActiveCooldownPowerConfiguration {
	public static final Codec<ActiveSelfConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("cooldown").forGetter(ActiveSelfConfiguration::duration),
			ApoliDataTypes.HUD_RENDER.fieldOf("hud_render").forGetter(ActiveSelfConfiguration::hudRender),
			ConfiguredEntityAction.CODEC.fieldOf("entity_action").forGetter(ActiveSelfConfiguration::action),
			CalioCodecHelper.optionalField(IActivePower.Key.BACKWARD_COMPATIBLE_CODEC, "key", IActivePower.Key.PRIMARY).forGetter(ActiveSelfConfiguration::key)
	).apply(instance, ActiveSelfConfiguration::new));
}
