package io.github.apace100.apoli.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.apace100.apoli.screen.GameHudRender;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
@Environment(EnvType.CLIENT)
public class InGameHudMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;getCurrentGameMode()Lnet/minecraft/world/GameMode;"))
    private void renderOnHud(PoseStack matrices, float tickDelta, CallbackInfo ci) {
        for(GameHudRender hudRender : GameHudRender.HUD_RENDERS) {
            hudRender.render(matrices, tickDelta);
        }
    }
}
