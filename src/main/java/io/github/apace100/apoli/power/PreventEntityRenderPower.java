package io.github.apace100.apoli.power;

import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class PreventEntityRenderPower extends Power {

    private final Predicate<LivingEntity> entityCondition;

    public PreventEntityRenderPower(PowerType<?> type, LivingEntity entity, Predicate<LivingEntity> entityCondition) {
        super(type, entity);
        this.entityCondition = entityCondition;
    }

    public boolean doesApply(Entity e) {
        return e instanceof LivingEntity && (entityCondition == null || entityCondition.test((LivingEntity)e));
    }
}
