package io.github.edwinmindcraft.apoli.common.condition.block;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.NoConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import java.util.Arrays;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class AttachableCondition extends BlockCondition<NoConfiguration> {

	public static final Codec<AttachableCondition> CODEC = Codec.unit(new AttachableCondition());

	public AttachableCondition() {
		super(NoConfiguration.CODEC);
	}

	@Override
	protected boolean check(NoConfiguration configuration, BlockInWorld block) {
		LevelReader world = block.getLevel();
		BlockPos pos = block.getPos();
		return Arrays.stream(Direction.values()).anyMatch(d -> world.getBlockState(pos.relative(d)).isFaceSturdy(world, pos, d.getOpposite()));
	}
}
