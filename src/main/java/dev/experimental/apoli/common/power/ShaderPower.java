package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import net.minecraft.resources.ResourceLocation;

public class ShaderPower extends PowerFactory<FieldConfiguration<ResourceLocation>> {

	public ShaderPower() {
		super(FieldConfiguration.codec(ResourceLocation.CODEC, "shader"));
	}
}
