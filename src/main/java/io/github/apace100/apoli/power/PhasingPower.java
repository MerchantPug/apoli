package io.github.apace100.apoli.power;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class PhasingPower extends Power {

    private final Predicate<BlockInWorld> blocks;
    private final boolean isBlacklist;

    private final Predicate<Player> phaseDownCondition;

    private final RenderType renderType;
    private final float viewDistance;

    public PhasingPower(PowerType<?> type, LivingEntity entity, Predicate<BlockInWorld> blocks, boolean isBlacklist,
                        RenderType renderType, float viewDistance, Predicate<Player> phaseDownCondition) {
        super(type, entity);
        this.blocks = blocks;
        this.isBlacklist = isBlacklist;
        this.renderType = renderType;
        this.viewDistance = viewDistance;
        this.phaseDownCondition = phaseDownCondition;
    }

    public boolean doesApply(BlockPos pos) {
        return isBlacklist != blocks.test(new BlockInWorld(entity.level, pos, true));
    }

    public boolean shouldPhaseDown(Player playerEntity) {
        return phaseDownCondition == null ? playerEntity.isShiftKeyDown() : phaseDownCondition.test(playerEntity);
    }

    public RenderType getRenderType() {
        return renderType;
    }

    public float getViewDistance() {
        return viewDistance;
    }

    public enum RenderType {
        BLINDNESS, REMOVE_BLOCKS
    }
}
