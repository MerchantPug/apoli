package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;

public class WalkOnFluidPower extends PowerFactory<FieldConfiguration<Tag<Fluid>>> {

	public WalkOnFluidPower() {
		super(FieldConfiguration.codec(SerializableDataTypes.FLUID_TAG, "fluid"));
	}
}
