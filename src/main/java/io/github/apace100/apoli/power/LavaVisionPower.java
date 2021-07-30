package io.github.apace100.apoli.power;

import net.minecraft.world.entity.LivingEntity;

public class LavaVisionPower extends Power {

    private final float s;
    private final float v;

    public LavaVisionPower(PowerType<?> type, LivingEntity entity, float s, float v) {
        super(type, entity);
        this.s = s;
        this.v = v;
    }

    public float getS() {
        return s;
    }

    public float getV() {
        return v;
    }
}
