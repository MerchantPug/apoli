package io.github.apace100.apoli.power;

import net.minecraft.world.entity.LivingEntity;

public class ModifyExhaustionPower extends ValueModifyingPower {

    public ModifyExhaustionPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }
}
