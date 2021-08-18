package dev.experimental.apoli.client;

import dev.experimental.apoli.client.screen.ApoliOverlay;
import io.github.apace100.apoli.screen.GameHudRender;
import io.github.apace100.apoli.screen.PowerHudRenderer;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;

public class ApoliClient {
	//TODO Check if this is correct.
	public static final IIngameOverlay APOLI_OVERLAY = OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Apoli Overlays", ApoliOverlay.INSTANCE);

	public static void initialize() {
		GameHudRender.HUD_RENDERS.add(new PowerHudRenderer());
	}
}
