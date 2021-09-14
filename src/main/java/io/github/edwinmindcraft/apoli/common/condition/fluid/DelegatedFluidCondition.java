package io.github.edwinmindcraft.apoli.common.condition.fluid;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.power.factory.FluidCondition;
import io.github.edwinmindcraft.apoli.common.condition.meta.IDelegatedConditionConfiguration;
import net.minecraft.world.level.material.FluidState;

public class DelegatedFluidCondition<T extends IDelegatedConditionConfiguration<FluidState>> extends FluidCondition<T> {
	public DelegatedFluidCondition(Codec<T> codec) {
		super(codec);
	}


	@Override
	public boolean check(T configuration, FluidState fluid) {
		return configuration.check(fluid);
	}
}
