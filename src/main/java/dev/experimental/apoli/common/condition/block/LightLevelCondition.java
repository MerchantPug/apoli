package dev.experimental.apoli.common.condition.block;

import dev.experimental.apoli.api.power.factory.BlockCondition;
import dev.experimental.apoli.common.condition.configuration.LightLevelConfiguration;
import net.minecraft.block.pattern.CachedBlockPosition;

public class LightLevelCondition extends BlockCondition<LightLevelConfiguration> {

	public LightLevelCondition() {
		super(LightLevelConfiguration.CODEC);
	}

	@Override
	protected boolean check(LightLevelConfiguration configuration, CachedBlockPosition block) {
		return configuration.comparison().check(configuration.getLightLevel(block.getWorld(), block.getBlockPos()));
	}
}
