package io.github.edwinmindcraft.apoli.api.power.configuration.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public interface IValueModifyingPowerConfiguration extends IDynamicFeatureConfiguration {
	ListConfiguration<ConfiguredModifier<?>> modifiers();
}
