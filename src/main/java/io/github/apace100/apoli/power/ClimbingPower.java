package io.github.apace100.apoli.power;

import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;

public class ClimbingPower extends Power {

    private final boolean allowHolding;
    private final Predicate<LivingEntity> holdingCondition;

    public ClimbingPower(PowerType<?> type, LivingEntity entity, boolean allowHolding, Predicate<LivingEntity> holdingCondition) {
        super(type, entity);
        this.allowHolding = allowHolding;
        this.holdingCondition = holdingCondition;
    }

    public boolean canHold() {
        return allowHolding && (holdingCondition == null ? isActive() : holdingCondition.test(entity));
    }
}
