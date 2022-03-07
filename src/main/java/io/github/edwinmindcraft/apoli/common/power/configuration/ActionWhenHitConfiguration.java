package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.ICooldownPowerConfiguration;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.HudRender;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ActionWhenHitConfiguration(int duration, HudRender hudRender,
										 @Nullable ConfiguredDamageCondition<?, ?> damageCondition,
										 ConfiguredEntityAction<?, ?> entityAction) implements ICooldownPowerConfiguration {
	public static final Codec<ActionWhenHitConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("cooldown").forGetter(ActionWhenHitConfiguration::duration),
			CalioCodecHelper.optionalField(ApoliDataTypes.HUD_RENDER, "hud_render", HudRender.DONT_RENDER).forGetter(ActionWhenHitConfiguration::hudRender),
			CalioCodecHelper.optionalField(ConfiguredDamageCondition.CODEC, "damage_condition").forGetter(x -> Optional.ofNullable(x.damageCondition())),
			ConfiguredEntityAction.CODEC.fieldOf("entity_action").forGetter(ActionWhenHitConfiguration::entityAction)
	).apply(instance, (t1, t2, t3, t4) -> new ActionWhenHitConfiguration(t1, t2, t3.orElse(null), t4)));
}
