package io.github.edwinmindcraft.apoli.api.power.factory.context;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.NonNullSupplier;

public record BlockConditionContext(LevelReader reader, BlockPos position, NonNullSupplier<BlockState> stateGetter) {
}
