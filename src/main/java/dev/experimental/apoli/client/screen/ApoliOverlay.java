package dev.experimental.apoli.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.apace100.apoli.screen.GameHudRender;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public enum ApoliOverlay implements IIngameOverlay {
	INSTANCE;

	@Override
	public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		for (GameHudRender hudRender : GameHudRender.HUD_RENDERS)
			hudRender.render(mStack, partialTicks);
	}
}
