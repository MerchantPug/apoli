package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IAttributeModifyingPowerConfiguration;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeModifyingPowerConfiguration(
		ListConfiguration<AttributeModifier> modifiers) implements IAttributeModifyingPowerConfiguration {
	public static Codec<AttributeModifyingPowerConfiguration> CODEC = ListConfiguration.ATTRIBUTE_CODEC
			.xmap(AttributeModifyingPowerConfiguration::new, AttributeModifyingPowerConfiguration::modifiers).codec();

	@Override
	public boolean isConfigurationValid() {
		return !this.modifiers().getContent().isEmpty();
	}
}
