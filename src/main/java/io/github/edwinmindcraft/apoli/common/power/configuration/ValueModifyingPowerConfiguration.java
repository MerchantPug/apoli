package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;

public record ValueModifyingPowerConfiguration(
        ListConfiguration<ConfiguredModifier<?>> modifiers) implements IValueModifyingPowerConfiguration {
    public static Codec<ValueModifyingPowerConfiguration> CODEC = ListConfiguration.MODIFIER_CODEC
            .xmap(ValueModifyingPowerConfiguration::new, ValueModifyingPowerConfiguration::modifiers).codec();

    @Override
    public boolean isConfigurationValid() {
        return !this.modifiers().getContent().isEmpty();
    }
}