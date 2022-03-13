package io.github.edwinmindcraft.apoli.common.power;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class OverrideHudTexturePower extends PowerFactory<FieldConfiguration<Optional<ResourceLocation>>> {
	public OverrideHudTexturePower() {
		super(FieldConfiguration.optionalCodec(SerializableDataTypes.IDENTIFIER, "texture"));
	}
}
