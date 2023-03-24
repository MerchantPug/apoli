package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.access.ReplacingLootContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootContext.Builder.class)
public class LootContextBuilderMixin {

    @Inject(method = "create", at = @At("RETURN"))
    private void setLootContextType(LootContextParamSet type, CallbackInfoReturnable<LootContext> cir) {
        ReplacingLootContext rlc = (ReplacingLootContext) cir.getReturnValue();
        rlc.setType(type);
    }
}