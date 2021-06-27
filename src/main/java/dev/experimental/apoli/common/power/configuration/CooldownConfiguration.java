package dev.experimental.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.power.configuration.power.ICooldownPowerConfiguration;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.HudRender;

public record CooldownConfiguration(int duration, HudRender hudRender) implements ICooldownPowerConfiguration {
	public static final Codec<CooldownConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("cooldown").forGetter(CooldownConfiguration::duration),
			ApoliDataTypes.HUD_RENDER.fieldOf("hud_render").forGetter(CooldownConfiguration::hudRender)
	).apply(instance, CooldownConfiguration::new));
}
