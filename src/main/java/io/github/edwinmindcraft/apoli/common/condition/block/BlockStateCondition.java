package io.github.edwinmindcraft.apoli.common.condition.block;

import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.BlockStateConfiguration;
import java.util.Collection;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockStateCondition extends BlockCondition<BlockStateConfiguration> {

	public BlockStateCondition() {
		super(BlockStateConfiguration.CODEC);
	}

	@Override
	protected boolean check(BlockStateConfiguration configuration, BlockInWorld block) {
		BlockState state = block.getState();
		Collection<Property<?>> properties = state.getProperties();
		return properties.stream().filter(p -> configuration.property().equals(p.getName())).findFirst().map(property -> configuration.checkProperty(state.getValue(property))).orElse(false);
	}
}

