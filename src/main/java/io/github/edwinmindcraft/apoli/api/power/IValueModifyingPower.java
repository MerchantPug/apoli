package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import java.util.List;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public interface IValueModifyingPower<T extends IDynamicFeatureConfiguration> {
	List<AttributeModifier> getModifiers(ConfiguredPower<T, ?> configuration, LivingEntity player);
}
