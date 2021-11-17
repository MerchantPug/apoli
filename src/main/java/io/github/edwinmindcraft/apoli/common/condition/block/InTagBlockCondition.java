package io.github.edwinmindcraft.apoli.common.condition.block;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.TagConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class InTagBlockCondition extends BlockCondition<TagConfiguration<Block>> {

	public InTagBlockCondition() {
		super(TagConfiguration.codec(SerializableDataTypes.BLOCK_TAG, "tag"));
	}

	@Override
	protected boolean check(TagConfiguration<Block> configuration, BlockInWorld block) {
		BlockState state = block.getState();
		if (state == null) return false; //World was not properly loaded yet.
		return configuration.contains(state.getBlock());
	}
}
