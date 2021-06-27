package dev.experimental.apoli.common.condition.block;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredFluidCondition;
import dev.experimental.apoli.api.power.factory.BlockCondition;
import net.minecraft.block.pattern.CachedBlockPosition;

public class FluidBlockCondition extends BlockCondition<FieldConfiguration<ConfiguredFluidCondition<?, ?>>> {

	public FluidBlockCondition() {
		super(FieldConfiguration.codec(ConfiguredFluidCondition.CODEC, "fluid_condition"));
	}

	@Override
	protected boolean check(FieldConfiguration<ConfiguredFluidCondition<?, ?>> configuration, CachedBlockPosition block) {
		return configuration.value().check(block.getBlockState().getFluidState());
	}
}
