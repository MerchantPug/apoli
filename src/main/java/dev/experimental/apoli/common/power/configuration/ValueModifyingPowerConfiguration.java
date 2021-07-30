package dev.experimental.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.configuration.ListConfiguration;
import dev.experimental.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;

public record ValueModifyingPowerConfiguration(
		ListConfiguration<EntityAttributeModifier> modifiers) implements IValueModifyingPowerConfiguration {
	public static Codec<ValueModifyingPowerConfiguration> CODEC = ListConfiguration.MODIFIER_CODEC
			.xmap(ValueModifyingPowerConfiguration::new, ValueModifyingPowerConfiguration::modifiers).codec();

	@Override
	public boolean isConfigurationValid() {
		return !this.modifiers().getContent().isEmpty();
	}
}
