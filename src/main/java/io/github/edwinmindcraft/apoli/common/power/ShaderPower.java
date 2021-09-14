package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.resources.ResourceLocation;

public class ShaderPower extends PowerFactory<FieldConfiguration<ResourceLocation>> {

	public ShaderPower() {
		super(FieldConfiguration.codec(ResourceLocation.CODEC, "shader"));
	}
}
