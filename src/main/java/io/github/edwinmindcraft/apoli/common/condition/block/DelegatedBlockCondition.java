package io.github.edwinmindcraft.apoli.common.condition.block;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import io.github.edwinmindcraft.apoli.common.condition.meta.IDelegatedConditionConfiguration;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class DelegatedBlockCondition<T extends IDelegatedConditionConfiguration<BlockInWorld>> extends BlockCondition<T> {
	public DelegatedBlockCondition(Codec<T> codec) {
		super(codec);
	}

	@Override
	protected boolean check(T configuration, BlockInWorld position) {
		return configuration.check(position);
	}
}
