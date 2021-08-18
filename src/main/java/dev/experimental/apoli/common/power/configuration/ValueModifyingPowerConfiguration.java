package dev.experimental.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.configuration.ListConfiguration;
import dev.experimental.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record ValueModifyingPowerConfiguration(
		ListConfiguration<AttributeModifier> modifiers) implements IValueModifyingPowerConfiguration {
	public static Codec<ValueModifyingPowerConfiguration> CODEC = ListConfiguration.MODIFIER_CODEC
			.xmap(ValueModifyingPowerConfiguration::new, ValueModifyingPowerConfiguration::modifiers).codec();

	@Override
	public boolean isConfigurationValid() {
		return !this.modifiers().getContent().isEmpty();
	}
}
