package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.util.HudRender;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.ICooldownPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record CombatHitActionConfiguration(int duration, HudRender hudRender,
										   @Nullable ConfiguredDamageCondition<?, ?> damageCondition,
										   @Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition,
										   ConfiguredBiEntityAction<?, ?> biEntityAction) implements ICooldownPowerConfiguration {
	public static final Codec<CombatHitActionConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(Codec.INT, "cooldown", 1).forGetter(CombatHitActionConfiguration::duration),
			CalioCodecHelper.optionalField(HudRender.CODEC, "hud_render", HudRender.DONT_RENDER).forGetter(CombatHitActionConfiguration::hudRender),
			CalioCodecHelper.optionalField(ConfiguredDamageCondition.CODEC, "damage_condition").forGetter(x -> Optional.ofNullable(x.damageCondition())),
			CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition").forGetter(x -> Optional.ofNullable(x.biEntityCondition())),
			ConfiguredBiEntityAction.CODEC.fieldOf("bientity_action").forGetter(CombatHitActionConfiguration::biEntityAction)
	).apply(instance, (t1, t2, t3, t4, t5) -> new CombatHitActionConfiguration(t1, t2, t3.orElse(null), t4.orElse(null), t5)));
}
