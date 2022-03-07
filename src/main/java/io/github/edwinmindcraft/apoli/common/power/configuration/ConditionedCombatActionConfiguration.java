package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.HudRender;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.ICooldownPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ConditionedCombatActionConfiguration(int duration, HudRender hudRender,
												   @Nullable ConfiguredDamageCondition<?, ?> damageCondition,
												   @Nullable ConfiguredEntityCondition<?, ?> targetCondition,
												   ConfiguredEntityAction<?, ?> entityAction) implements ICooldownPowerConfiguration {
	public static final Codec<ConditionedCombatActionConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("cooldown").forGetter(ConditionedCombatActionConfiguration::duration),
			CalioCodecHelper.optionalField(ApoliDataTypes.HUD_RENDER, "hud_render", HudRender.DONT_RENDER).forGetter(ConditionedCombatActionConfiguration::hudRender),
			CalioCodecHelper.optionalField(ConfiguredDamageCondition.CODEC, "damage_condition").forGetter(x -> Optional.ofNullable(x.damageCondition())),
			CalioCodecHelper.optionalField(ConfiguredEntityCondition.CODEC, "target_condition").forGetter(x -> Optional.ofNullable(x.targetCondition())),
			ConfiguredEntityAction.CODEC.fieldOf("entity_action").forGetter(ConditionedCombatActionConfiguration::entityAction)
	).apply(instance, (t1, t2, t3, t4, t5) -> new ConditionedCombatActionConfiguration(t1, t2, t3.orElse(null), t4.orElse(null), t5)));

	public boolean check(LivingEntity target, DamageSource source, float amount) {
		return ConfiguredEntityCondition.check(this.targetCondition(), target) && ConfiguredDamageCondition.check(this.damageCondition(), source, amount);
	}
}
