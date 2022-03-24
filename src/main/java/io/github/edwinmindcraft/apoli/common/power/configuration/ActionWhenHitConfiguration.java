package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.util.HudRender;
import io.github.edwinmindcraft.apoli.api.configuration.MustBeBound;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.ICooldownPowerConfiguration;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.NotNull;

public record ActionWhenHitConfiguration(ICooldownPowerConfiguration cooldown,
										 Holder<ConfiguredDamageCondition<?, ?>> damageCondition,
										 @MustBeBound Holder<ConfiguredEntityAction<?, ?>> entityAction) implements ICooldownPowerConfiguration {
	public static final Codec<ActionWhenHitConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ICooldownPowerConfiguration.MAP_CODEC.forGetter(ActionWhenHitConfiguration::cooldown),
			ConfiguredDamageCondition.optional("damage_condition").forGetter(ActionWhenHitConfiguration::damageCondition),
			ConfiguredEntityAction.required("entity_action").forGetter(ActionWhenHitConfiguration::entityAction)
	).apply(instance, ActionWhenHitConfiguration::new));

	@Override
	public int duration() {
		return this.cooldown().duration();
	}

	@Override
	public @NotNull HudRender hudRender() {
		return this.cooldown().hudRender();
	}
}
