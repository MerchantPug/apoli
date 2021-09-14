package io.github.edwinmindcraft.apoli.common.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;

/**
 * @deprecated Unused in the original code of origins, not registered anywhere, so I'm skipping for now.
 */
@Deprecated
public class FloatPower extends PowerFactory<FieldConfiguration<Float>> {

	public FloatPower() {
		super(FieldConfiguration.codec(Codec.FLOAT, "value"));
	}
}
