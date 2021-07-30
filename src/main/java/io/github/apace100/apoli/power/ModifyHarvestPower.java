package io.github.apace100.apoli.power;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class ModifyHarvestPower extends Power {

    private final Predicate<BlockInWorld> predicate;
    private boolean allow;

    public ModifyHarvestPower(PowerType<?> type, LivingEntity entity, Predicate<BlockInWorld> predicate, boolean allow) {
        super(type, entity);
        this.predicate = predicate;
        this.allow = allow;
    }

    public boolean doesApply(BlockPos pos) {
        BlockInWorld cbp = new BlockInWorld(entity.level, pos, true);
        return predicate.test(cbp);
    }

    public boolean doesApply(BlockInWorld pos) {
        return predicate.test(pos);
    }

    public boolean isHarvestAllowed() {
        return allow;
    }
}
