package dev.experimental.apoli.api.power;

import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface ICooldownPower<T extends IDynamicFeatureConfiguration> extends IVariableIntPower<T> {
	void use(ConfiguredPower<T, ?> configuration, LivingEntity player);

	boolean canUse(ConfiguredPower<T, ?> configuration, LivingEntity player);
}
