package dev.experimental.apoli.api.power.configuration.power;

import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.configuration.ListConfiguration;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public interface IValueModifyingPowerConfiguration extends IDynamicFeatureConfiguration {
	ListConfiguration<AttributeModifier> modifiers();
}
