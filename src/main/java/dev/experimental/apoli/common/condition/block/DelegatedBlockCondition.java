package dev.experimental.apoli.common.condition.block;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.power.factory.BlockCondition;
import dev.experimental.apoli.common.condition.meta.IDelegatedConditionConfiguration;
import net.minecraft.block.pattern.CachedBlockPosition;

public class DelegatedBlockCondition<T extends IDelegatedConditionConfiguration<CachedBlockPosition>> extends BlockCondition<T> {
	public DelegatedBlockCondition(Codec<T> codec) {
		super(codec);
	}

	@Override
	protected boolean check(T configuration, CachedBlockPosition position) {
		return configuration.check(position);
	}
}
