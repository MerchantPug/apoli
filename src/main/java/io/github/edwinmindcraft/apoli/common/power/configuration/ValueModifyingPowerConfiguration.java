package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
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
