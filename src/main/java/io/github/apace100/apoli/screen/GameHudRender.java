package io.github.apace100.apoli.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public interface GameHudRender {

    List<GameHudRender> HUD_RENDERS = new ArrayList<>();

    void render(PoseStack matrixStack, float tickDelta);
}