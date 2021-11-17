package io.github.edwinmindcraft.apoli.common.power;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.TagConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.level.material.Fluid;

public class WalkOnFluidPower extends PowerFactory<TagConfiguration<Fluid>> {

	public WalkOnFluidPower() {
		super(TagConfiguration.codec(SerializableDataTypes.FLUID_TAG, "fluid"));
	}
}
