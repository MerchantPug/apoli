package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

/**
 * @deprecated Unused in the original code of origins, not registered anywhere, so I'm skipping for now.
 */
@Deprecated
public class FloatPower extends PowerFactory<FieldConfiguration<Float>> {

	public FloatPower() {
		super(FieldConfiguration.codec(CalioCodecHelper.FLOAT, "value"));
	}
}
