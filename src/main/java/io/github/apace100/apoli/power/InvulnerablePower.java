package io.github.apace100.apoli.power;

import java.util.function.Predicate;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class InvulnerablePower extends Power {

    private final Predicate<DamageSource> damageSources;

    public InvulnerablePower(PowerType<?> type, LivingEntity entity, Predicate<DamageSource> damageSourcePredicate) {
        super(type, entity);
        this.damageSources = damageSourcePredicate;
    }

    public boolean doesApply(DamageSource source) {
        return damageSources.test(source);
    }
}
