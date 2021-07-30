package io.github.apace100.apoli.power;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class StackingStatusEffectPower extends StatusEffectPower {

    private final int minStack;
    private final int maxStack;
    private final int durationPerStack;

    private int currentStack;

    public StackingStatusEffectPower(PowerType<?> type, LivingEntity entity, int minStack, int maxStack, int durationPerStack) {
        super(type, entity);
        this.minStack = minStack;
        this.maxStack = maxStack;
        this.durationPerStack = durationPerStack;
        this.setTicking(true);
    }

    public void tick() {
        if(entity.tickCount % 10 == 0) {
            if(isActive()) {
                currentStack += 1;
                if(currentStack > maxStack) {
                    currentStack = maxStack;
                }
                if(currentStack > 0) {
                    applyEffects();
                }
            } else {
                currentStack -= 1;
                if(currentStack < minStack) {
                    currentStack = minStack;
                }
            }
        }
    }

    @Override
    public void applyEffects() {
        effects.forEach(sei -> {
            int duration = durationPerStack * currentStack;
            if(duration > 0) {
                MobEffectInstance applySei = new MobEffectInstance(sei.getEffect(), duration, sei.getAmplifier(), sei.isAmbient(), sei.isVisible(), sei.showIcon());
                entity.addEffect(applySei);
            }
        });
    }

    @Override
    public Tag toTag() {
        return IntTag.valueOf(currentStack);
    }

    @Override
    public void fromTag(Tag tag) {
        currentStack = ((IntTag)tag).getAsInt();
    }
}
