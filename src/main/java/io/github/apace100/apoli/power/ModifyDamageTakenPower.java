package io.github.apace100.apoli.power;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ModifyDamageTakenPower extends ValueModifyingPower {

    private final Predicate<Tuple<DamageSource, Float>> condition;

    private Consumer<LivingEntity> attackerAction;
    private Consumer<LivingEntity> selfAction;

    public ModifyDamageTakenPower(PowerType<?> type, LivingEntity entity, Predicate<Tuple<DamageSource, Float>> condition) {
        super(type, entity);
        this.condition = condition;
    }

    public boolean doesApply(DamageSource source, float damageAmount) {
        return condition.test(new Tuple(source, damageAmount));
    }

    public void setAttackerAction(Consumer<LivingEntity> attackerAction) {
        this.attackerAction = attackerAction;
    }

    public void setSelfAction(Consumer<LivingEntity> selfAction) {
        this.selfAction = selfAction;
    }

    public void executeActions(Entity attacker) {
        if(selfAction != null) {
            selfAction.accept(entity);
        }
        if(attackerAction != null && attacker instanceof LivingEntity) {
            attackerAction.accept((LivingEntity)attacker);
        }
    }
}
