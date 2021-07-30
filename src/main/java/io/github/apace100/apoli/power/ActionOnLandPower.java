package io.github.apace100.apoli.power;

import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ActionOnLandPower extends Power {

    private final Consumer<Entity> entityAction;

    public ActionOnLandPower(PowerType<?> type, LivingEntity entity, Consumer<Entity> entityAction) {
        super(type, entity);
        this.entityAction = entityAction;
    }

    public void executeAction() {
        entityAction.accept(entity);
    }
}
