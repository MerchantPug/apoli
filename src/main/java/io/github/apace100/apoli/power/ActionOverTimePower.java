package io.github.apace100.apoli.power;

import java.util.function.Consumer;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ActionOverTimePower extends Power {

    private final int interval;
    private final Consumer<Entity> entityAction;
    private final Consumer<Entity> risingAction;
    private final Consumer<Entity> fallingAction;

    private boolean wasActive = false;

    public ActionOverTimePower(PowerType<?> type, LivingEntity entity, int interval, Consumer<Entity> entityAction, Consumer<Entity> risingAction, Consumer<Entity> fallingAction) {
        super(type, entity);
        this.interval = interval;
        this.entityAction = entityAction;
        this.risingAction = risingAction;
        this.fallingAction = fallingAction;
        this.setTicking(true);
    }

    public void tick() {
        if(entity.tickCount % interval == 0) {
            if (isActive()) {
                if (entityAction != null) {
                    entityAction.accept(entity);
                }
                if (!wasActive && risingAction != null) {
                    risingAction.accept(entity);
                }
                wasActive = true;
            } else {
                if (wasActive && fallingAction != null) {
                    fallingAction.accept(entity);
                }
                wasActive = false;
            }
        }
    }

    @Override
    public Tag toTag() {
        return ByteTag.valueOf(wasActive);
    }

    @Override
    public void fromTag(Tag tag) {
        wasActive = tag.equals(ByteTag.ONE);
    }
}
