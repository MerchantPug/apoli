package io.github.apace100.apoli.access;

import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;

public interface SubmergableEntity {

    boolean isSubmergedInLoosely(Tag<Fluid> fluidTag);

    double getFluidHeightLoosely(Tag<Fluid> fluidTag);
}
