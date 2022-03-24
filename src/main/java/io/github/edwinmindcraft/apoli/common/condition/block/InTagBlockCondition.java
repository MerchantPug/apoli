package io.github.edwinmindcraft.apoli.common.condition.block;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.TagConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.NonNullSupplier;

public class InTagBlockCondition extends BlockCondition<TagConfiguration<Block>> {

	public InTagBlockCondition() {
		super(TagConfiguration.codec(SerializableDataTypes.BLOCK_TAG, "tag"));
	}

	@Override
	protected boolean check(TagConfiguration<Block> configuration, LevelReader reader, BlockPos position, NonNullSupplier<BlockState> stateGetter) {
		return stateGetter.get().is(configuration.value());
	}
}
