package io.github.edwinmindcraft.apoli.common.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyStatusEffectConfiguration;
import net.minecraft.world.effect.MobEffect;

import java.util.Objects;

public class ModifyStatusEffectPower extends ValueModifyingPowerFactory<ModifyStatusEffectConfiguration> {

	public static boolean doesApply(ConfiguredPower<ModifyStatusEffectConfiguration, ?> power, MobEffect effect) {
		return Objects.equals(power.getConfiguration().effect(), effect);
	}

	public ModifyStatusEffectPower() {
		super(ModifyStatusEffectConfiguration.CODEC);
	}
}
