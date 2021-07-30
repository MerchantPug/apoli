package dev.experimental.apoli.common.action.block;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.common.action.meta.IDelegatedActionConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import dev.experimental.apoli.api.power.factory.BlockAction;
import org.apache.commons.lang3.tuple.Triple;

public class DelegatedBlockAction<T extends IDelegatedActionConfiguration<Triple<Level, BlockPos, Direction>>> extends BlockAction<T> {

	public DelegatedBlockAction(Codec<T> codec) {
		super(codec);
	}

	@Override
	public void execute(T configuration, Level world, BlockPos pos, Direction direction) {
		configuration.execute(Triple.of(world, pos, direction));
	}
}
