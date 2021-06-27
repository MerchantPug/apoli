package dev.experimental.apoli.common.condition.block;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.BlockCondition;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.block.Block;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.tag.Tag;

public class InTagBlockCondition extends BlockCondition<FieldConfiguration<Tag<Block>>> {

	public InTagBlockCondition() {
		super(FieldConfiguration.codec(SerializableDataTypes.BLOCK_TAG, "tag"));
	}

	@Override
	protected boolean check(FieldConfiguration<Tag<Block>> configuration, CachedBlockPosition block) {
		return block.getBlockState().isIn(configuration.value());
	}
}
