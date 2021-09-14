package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class EffectImmunityPower extends PowerFactory<ListConfiguration<MobEffect>> {
	public static boolean isImmune(LivingEntity player, MobEffectInstance effect) {
		return IPowerContainer.getPowers(player, ModPowers.EFFECT_IMMUNITY.get()).stream().anyMatch(x -> x.getFactory().isImmune(x, player, effect));
	}

	public EffectImmunityPower() {
		super(ListConfiguration.codec(SerializableDataTypes.STATUS_EFFECT, "effect", "effects"));
	}

	public boolean isImmune(ConfiguredPower<ListConfiguration<MobEffect>, ?> configuration, LivingEntity player, MobEffect effect) {
		return configuration.getConfiguration().getContent().contains(effect);
	}

	public boolean isImmune(ConfiguredPower<ListConfiguration<MobEffect>, ?> configuration, LivingEntity player, MobEffectInstance effect) {
		return this.isImmune(configuration, player, effect.getEffect());
	}
}
