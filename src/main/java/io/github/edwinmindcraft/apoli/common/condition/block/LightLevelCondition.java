package io.github.edwinmindcraft.apoli.common.condition.block;

import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.LightLevelConfiguration;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class LightLevelCondition extends BlockCondition<LightLevelConfiguration> {

	public LightLevelCondition() {
		super(LightLevelConfiguration.CODEC);
	}

	@Override
	protected boolean check(LightLevelConfiguration configuration, BlockInWorld block) {
		return configuration.comparison().check(configuration.getLightLevel(block.getLevel(), block.getPos()));
	}
}
