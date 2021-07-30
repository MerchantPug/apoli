package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;

public class WalkOnFluidPower extends PowerFactory<FieldConfiguration<Tag<Fluid>>> {

	public WalkOnFluidPower() {
		super(FieldConfiguration.codec(SerializableDataTypes.FLUID_TAG, "fluid"));
	}
}
