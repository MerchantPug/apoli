package io.github.edwinmindcraft.apoli.common.action.block;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import io.github.edwinmindcraft.apoli.common.action.configuration.DistanceFromPointConfiguration;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.Vec3;

public class DistanceFromPointBlockCondition extends BlockCondition<DistanceFromPointConfiguration> {
	public DistanceFromPointBlockCondition(Codec<DistanceFromPointConfiguration> codec) {
		super(codec);
	}

	@Override
	protected boolean check(DistanceFromPointConfiguration configuration, BlockInWorld block) {
		return block.getLevel() instanceof Level level ? configuration.test(null, Vec3.atLowerCornerOf(block.getPos()), level) : configuration.comparison().check(Double.POSITIVE_INFINITY);
	}
}
