package io.github.edwinmindcraft.apoli.common.action.block;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockAction;
import io.github.edwinmindcraft.apoli.common.action.meta.IDelegatedActionConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
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
