package io.github.apace100.apoli.power;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ModifyDamageDealtPower extends ValueModifyingPower {

    private final Predicate<Tuple<DamageSource, Float>> condition;
    private final Predicate<LivingEntity> targetCondition;

    private Consumer<LivingEntity> targetAction;
    private Consumer<LivingEntity> selfAction;

    public ModifyDamageDealtPower(PowerType<?> type, LivingEntity entity, Predicate<Tuple<DamageSource, Float>> condition, Predicate<LivingEntity> targetCondition) {
        super(type, entity);
        this.condition = condition;
        this.targetCondition = targetCondition;
    }

    public boolean doesApply(DamageSource source, float damageAmount, LivingEntity target) {
        return condition.test(new Tuple<>(source, damageAmount)) && (target == null || targetCondition == null || targetCondition.test(target));
    }

    public void setTargetAction(Consumer<LivingEntity> targetAction) {
        this.targetAction = targetAction;
    }

    public void setSelfAction(Consumer<LivingEntity> selfAction) {
        this.selfAction = selfAction;
    }

    public void executeActions(Entity target) {
        if(selfAction != null) {
            selfAction.accept(entity);
        }
        if(targetAction != null && target instanceof LivingEntity) {
            targetAction.accept((LivingEntity)target);
        }
    }
}
