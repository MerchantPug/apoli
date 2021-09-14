package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.world.entity.LivingEntity;

public interface ICooldownPower<T extends IDynamicFeatureConfiguration> extends IVariableIntPower<T> {
	void use(ConfiguredPower<T, ?> configuration, LivingEntity player);

	boolean canUse(ConfiguredPower<T, ?> configuration, LivingEntity player);
}
