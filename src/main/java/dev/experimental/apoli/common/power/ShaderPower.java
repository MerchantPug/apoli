package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import net.minecraft.util.Identifier;

public class ShaderPower extends PowerFactory<FieldConfiguration<Identifier>> {

	public ShaderPower() {
		super(FieldConfiguration.codec(Identifier.CODEC, "shader"));
	}
}
