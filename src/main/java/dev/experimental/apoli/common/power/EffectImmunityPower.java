package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.ListConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.registry.ModPowers;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public class EffectImmunityPower extends PowerFactory<ListConfiguration<MobEffect>> {
	public static boolean isImmune(Player player, MobEffectInstance effect) {
		return IPowerContainer.getPowers(player, ModPowers.EFFECT_IMMUNITY.get()).stream().anyMatch(x -> x.getFactory().isImmune(x, player, effect));
	}

	public EffectImmunityPower() {
		super(ListConfiguration.codec(SerializableDataTypes.STATUS_EFFECT, "effect", "effects"));
	}

	public boolean isImmune(ConfiguredPower<ListConfiguration<MobEffect>, ?> configuration, Player player, MobEffect effect) {
		return configuration.getConfiguration().getContent().contains(effect);
	}

	public boolean isImmune(ConfiguredPower<ListConfiguration<MobEffect>, ?> configuration, Player player, MobEffectInstance effect) {
		return this.isImmune(configuration, player, effect.getEffect());
	}
}
