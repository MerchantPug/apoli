package io.github.apace100.apoli.power;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ExhaustOverTimePower extends Power {

    private final int exhaustInterval;
    private final float exhaustion;

    public ExhaustOverTimePower(PowerType<?> type, LivingEntity entity, int exhaustInterval, float exhaustion) {
        super(type, entity);
        this.exhaustInterval = exhaustInterval;
        this.exhaustion = exhaustion;
        this.setTicking();
    }

    public void tick() {
        if(entity instanceof Player && entity.tickCount % exhaustInterval == 0) {
            ((Player)entity).causeFoodExhaustion(exhaustion);
        }
    }
}
