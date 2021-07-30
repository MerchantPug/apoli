package io.github.apace100.apoli.power;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class PreventDeathPower extends Power {

    private final Consumer<Entity> entityAction;
    private final Predicate<Tuple<DamageSource, Float>> condition;

    public PreventDeathPower(PowerType<?> type, LivingEntity entity, Consumer<Entity> entityAction, Predicate<Tuple<DamageSource, Float>> condition) {
        super(type, entity);
        this.entityAction = entityAction;
        this.condition = condition;
    }

    public boolean doesApply(DamageSource source, float amount) {
        return condition == null || condition.test(new Tuple<>(source, amount));
    }

    public void executeAction() {
        if(entityAction != null) {
            entityAction.accept(entity);
        }
    }
}
