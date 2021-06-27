package dev.experimental.apoli.api.power.configuration.power;

import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.configuration.ListConfiguration;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public interface IValueModifyingPowerConfiguration extends IDynamicFeatureConfiguration {
	ListConfiguration<EntityAttributeModifier> modifiers();
}
