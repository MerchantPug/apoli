package io.github.apace100.apoli.power;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class PreventBlockUsePower extends Power {

    private final Predicate<BlockInWorld> predicate;

    public PreventBlockUsePower(PowerType<?> type, LivingEntity entity, Predicate<BlockInWorld> predicate) {
        super(type, entity);
        this.predicate = predicate;
    }

    public boolean doesPrevent(LevelReader world, BlockPos pos) {
        BlockInWorld cbp = new BlockInWorld(world, pos, true);
        return predicate.test(cbp);
    }
}
