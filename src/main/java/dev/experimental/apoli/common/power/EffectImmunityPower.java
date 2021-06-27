package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.ListConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.registry.ModPowers;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

public class EffectImmunityPower extends PowerFactory<ListConfiguration<StatusEffect>> {
	public static boolean isImmune(PlayerEntity player, StatusEffectInstance effect) {
		return IPowerContainer.getPowers(player, ModPowers.EFFECT_IMMUNITY.get()).stream().anyMatch(x -> x.getFactory().isImmune(x, player, effect));
	}

	public EffectImmunityPower() {
		super(ListConfiguration.codec(SerializableDataTypes.STATUS_EFFECT, "effect", "effects"));
	}

	public boolean isImmune(ConfiguredPower<ListConfiguration<StatusEffect>, ?> configuration, PlayerEntity player, StatusEffect effect) {
		return configuration.getConfiguration().getContent().contains(effect);
	}

	public boolean isImmune(ConfiguredPower<ListConfiguration<StatusEffect>, ?> configuration, PlayerEntity player, StatusEffectInstance effect) {
		return this.isImmune(configuration, player, effect.getEffectType());
	}
}
