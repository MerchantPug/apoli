package dev.experimental.apoli.common.condition.block;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.power.factory.BlockCondition;
import dev.experimental.apoli.common.condition.meta.IDelegatedConditionConfiguration;
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
