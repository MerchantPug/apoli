package io.github.edwinmindcraft.apoli.common.condition.block;

import io.github.edwinmindcraft.apoli.api.configuration.NoConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import java.util.function.Predicate;

import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class SimpleBlockCondition extends BlockCondition<NoConfiguration> {

	public static final Predicate<BlockInWorld> REPLACEABLE = t -> t.getState().getMaterial().isReplaceable();
	public static final Predicate<BlockInWorld> MOVEMENT_BLOCKING = t -> t.getState().getMaterial().blocksMotion() && !t.getState().getCollisionShape(t.getLevel(), t.getPos()).isEmpty();
	public static final Predicate<BlockInWorld> LIGHT_BLOCKING = t -> t.getState().getMaterial().isSolidBlocking();
	public static final Predicate<BlockInWorld> WATER_LOGGABLE = t -> t.getState().getBlock() instanceof LiquidBlockContainer;
	public static final Predicate<BlockInWorld> EXPOSED_TO_SKY = t -> t.getLevel().canSeeSky(t.getPos());

	private final Predicate<BlockInWorld> predicate;

	public SimpleBlockCondition(Predicate<BlockInWorld> predicate) {
		super(NoConfiguration.CODEC);
		this.predicate = predicate;
	}

	@Override
	public boolean check(NoConfiguration configuration, BlockInWorld block) {
		return this.predicate.test(block);
	}
}
