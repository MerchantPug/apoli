package io.github.apace100.apoli.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public interface GameHudRender {

	List<GameHudRender> HUD_RENDERS = new ArrayList<>();

	void render(PoseStack matrixStack, float tickDelta);
}