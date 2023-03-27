package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyStatusEffectConfiguration;
import net.minecraft.world.effect.MobEffect;

import java.util.Objects;

public class ModifyStatusEffectPower extends ValueModifyingPowerFactory<ModifyStatusEffectConfiguration> {

	public static boolean doesApply(ConfiguredPower<ModifyStatusEffectConfiguration, ?> power, MobEffect effect) {
		return power.getConfiguration().effects().getContent().isEmpty() || power.getConfiguration().effects().getContent().contains(effect);
	}

	public ModifyStatusEffectPower() {
		super(ModifyStatusEffectConfiguration.CODEC);
	}
}
