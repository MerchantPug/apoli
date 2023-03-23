package io.github.apace100.apoli.mixin.forge;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "net/minecraft/world/inventory/GrindstoneMenu$4")
public interface GrindstoneMenuResultSlotAccessor {
    @Invoker
    int invokeGetExperienceAmount(Level level);
}
