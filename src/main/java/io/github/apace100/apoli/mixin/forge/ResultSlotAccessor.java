package io.github.apace100.apoli.mixin.forge;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ResultSlot.class)
public interface ResultSlotAccessor {
    @Accessor
    CraftingContainer getCraftSlots();
}
