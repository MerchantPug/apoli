package io.github.apace100.apoli.power;

import java.util.HashSet;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class EffectImmunityPower extends Power {

    protected final HashSet<MobEffect> effects = new HashSet<>();

    public EffectImmunityPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }
    public EffectImmunityPower(PowerType<?> type, LivingEntity entity, MobEffect effect) {
        super(type, entity);
        addEffect(effect);
    }

    public EffectImmunityPower addEffect(MobEffect effect) {
        effects.add(effect);
        return this;
    }

    public boolean doesApply(MobEffectInstance instance) {
        return doesApply(instance.getEffect());
    }

    public boolean doesApply(MobEffect effect) {
        return effects.contains(effect);
    }
}
