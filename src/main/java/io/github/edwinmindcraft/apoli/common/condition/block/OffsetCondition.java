package io.github.edwinmindcraft.apoli.common.condition.block;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import io.github.edwinmindcraft.apoli.common.action.configuration.OffsetConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.NonNullSupplier;

public class OffsetCondition extends BlockCondition<OffsetConfiguration<ConfiguredBlockCondition<?, ?>>> {

	public OffsetCondition() {
		super(OffsetConfiguration.codec("condition", ConfiguredBlockCondition.CODEC));
	}

	@Override
	protected boolean check(OffsetConfiguration<ConfiguredBlockCondition<?, ?>> configuration, LevelReader reader, BlockPos position, NonNullSupplier<BlockState> stateGetter) {
		BlockPos target = position.offset(configuration.asBlockPos());
		return configuration.value().check(reader, target, () -> reader.getBlockState(target));
	}
}
