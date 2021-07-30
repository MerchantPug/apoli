package io.github.apace100.apoli.power;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class PreventSleepPower extends Power {

    private final Predicate<BlockInWorld> predicate;
    private final String message;
    private final boolean allowSpawnPoint;

    public PreventSleepPower(PowerType<?> type, LivingEntity entity, Predicate<BlockInWorld> predicate, String message, boolean allowSpawnPoint) {
        super(type, entity);
        this.predicate = predicate;
        this.message = message;
        this.allowSpawnPoint = allowSpawnPoint;
    }

    public boolean doesPrevent(LevelReader world, BlockPos pos) {
        BlockInWorld cbp = new BlockInWorld(world, pos, true);
        return predicate.test(cbp);
    }

    public String getMessage() {
        return message;
    }

    public boolean doesAllowSpawnPoint() {
        return allowSpawnPoint;
    }
}
