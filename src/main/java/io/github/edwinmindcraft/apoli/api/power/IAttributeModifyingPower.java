package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

public interface IAttributeModifyingPower<T extends IDynamicFeatureConfiguration> {
	List<AttributeModifier> getModifiers(ConfiguredPower<T, ?> configuration, Entity player);
}
