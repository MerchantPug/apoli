package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.ICooldownPowerConfiguration;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.HudRender;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ActionWhenHitConfiguration(int duration, HudRender hudRender,
										 @Nullable ConfiguredDamageCondition<?, ?> damageCondition,
										 ConfiguredEntityAction<?, ?> entityAction) implements ICooldownPowerConfiguration {
	public static final Codec<ActionWhenHitConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("cooldown").forGetter(ActionWhenHitConfiguration::duration),
			ApoliDataTypes.HUD_RENDER.optionalFieldOf("hud_render", HudRender.DONT_RENDER).forGetter(ActionWhenHitConfiguration::hudRender),
			ConfiguredDamageCondition.CODEC.optionalFieldOf("damage_condition").forGetter(x -> Optional.ofNullable(x.damageCondition())),
			ConfiguredEntityAction.CODEC.fieldOf("entity_action").forGetter(ActionWhenHitConfiguration::entityAction)
	).apply(instance, (t1, t2, t3, t4) -> new ActionWhenHitConfiguration(t1, t2, t3.orElse(null), t4)));
}
