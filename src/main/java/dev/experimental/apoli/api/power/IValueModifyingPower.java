package dev.experimental.apoli.api.power;

import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface IValueModifyingPower<T extends IDynamicFeatureConfiguration> {
	List<EntityAttributeModifier> getModifiers(ConfiguredPower<T, ?> configuration, LivingEntity player);
}
