package io.github.edwinmindcraft.apoli.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.apace100.apoli.screen.GameHudRender;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public enum ApoliOverlay implements IGuiOverlay {
	INSTANCE;

	@Override
	public void render(ForgeGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		if (!Minecraft.getInstance().options.hideGui) {
			for (GameHudRender hudRender : GameHudRender.HUD_RENDERS)
				hudRender.render(mStack, partialTicks);
		}
	}
}
