package dev.experimental.apoli.common.condition.block;

import dev.experimental.apoli.common.action.configuration.OffsetConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockCondition;
import dev.experimental.apoli.api.power.factory.BlockCondition;
import net.minecraft.block.pattern.CachedBlockPosition;

public class OffsetCondition extends BlockCondition<OffsetConfiguration<ConfiguredBlockCondition<?, ?>>> {

	public OffsetCondition() {
		super(OffsetConfiguration.codec("condition", ConfiguredBlockCondition.CODEC));
	}

	@Override
	protected boolean check(OffsetConfiguration<ConfiguredBlockCondition<?, ?>> configuration, CachedBlockPosition block) {
		return configuration.value().check(new CachedBlockPosition(block.getWorld(), block.getBlockPos().add(configuration.asBlockPos()), true));
	}
}
