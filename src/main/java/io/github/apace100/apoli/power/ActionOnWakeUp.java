package io.github.apace100.apoli.power;

import org.apache.commons.lang3.tuple.Triple;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class ActionOnWakeUp extends Power {

    private final Predicate<BlockInWorld> blockCondition;
    private final Consumer<Entity> entityAction;
    private final Consumer<Triple<Level, BlockPos, Direction>> blockAction;

    public ActionOnWakeUp(PowerType<?> type, LivingEntity entity, Predicate<BlockInWorld> blockCondition, Consumer<Entity> entityAction, Consumer<Triple<Level, BlockPos, Direction>> blockAction) {
        super(type, entity);
        this.blockCondition = blockCondition;
        this.entityAction = entityAction;
        this.blockAction = blockAction;
    }

    public boolean doesApply(BlockPos pos) {
        BlockInWorld cbp = new BlockInWorld(entity.level, pos, true);
        return doesApply(cbp);
    }

    public boolean doesApply(BlockInWorld pos) {
        return blockCondition == null || blockCondition.test(pos);
    }

    public void executeActions(BlockPos pos, Direction dir) {
        if(blockAction != null) {
            blockAction.accept(Triple.of(entity.level, pos, dir));
        }
        if(entityAction != null) {
            entityAction.accept(entity);
        }
    }
}
