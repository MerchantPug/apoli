package io.github.apace100.apoli.power;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;

public class SetEntityGroupPower extends Power {

    public final MobType group;

    public SetEntityGroupPower(PowerType<?> type, LivingEntity entity, MobType group) {
        super(type, entity);
        this.group = group;
    }
}
