package io.github.apace100.apoli.power;

import net.minecraft.tags.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;

public class WalkOnFluidPower extends Power {

    private final Tag<Fluid> fluidTag;

    public WalkOnFluidPower(PowerType<?> type, LivingEntity entity, Tag<Fluid> fluidTag) {
        super(type, entity);
        this.fluidTag = fluidTag;
    }

    public Tag<Fluid> getFluidTag() {
        return fluidTag;
    }
}
