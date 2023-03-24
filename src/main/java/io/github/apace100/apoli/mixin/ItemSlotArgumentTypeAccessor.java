package io.github.apace100.apoli.mixin;

import net.minecraft.commands.arguments.SlotArgument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(SlotArgument.class)
public interface ItemSlotArgumentTypeAccessor {

    @Accessor("SLOTS")
    Map<String, Integer> getSlotNamesToSlotCommandId();

}