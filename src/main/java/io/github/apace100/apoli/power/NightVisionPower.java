package io.github.apace100.apoli.power;

import java.util.function.Function;
import net.minecraft.world.entity.LivingEntity;

public class NightVisionPower extends Power {

    private final Function<LivingEntity, Float> strengthFunction;

    public NightVisionPower(PowerType<?> type, LivingEntity entity) {
        this(type, entity, 1.0F);
    }

    public NightVisionPower(PowerType<?> type, LivingEntity entity, float strength) {
        this(type, entity, pe -> strength);
    }

    public NightVisionPower(PowerType<?> type, LivingEntity entity, Function<LivingEntity, Float> strengthFunction) {
        super(type, entity);
        this.strengthFunction = strengthFunction;
    }

    public float getStrength() {
        return strengthFunction.apply(this.entity);
    }
}
