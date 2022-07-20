package io.github.edwinmindcraft.apoli.client.screen;

import io.github.edwinmindcraft.apoli.common.power.configuration.OverlayConfiguration;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

public class ApoliOverlays {
	public static void bootstrap() {

	}

	public static void registerOverlays(RegisterGuiOverlaysEvent event) {
		event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "overlay", ApoliOverlay.INSTANCE);
		event.registerBelowAll("bottom_overlay", new ApoliPowerOverlay(configuration -> configuration.phase() == OverlayConfiguration.DrawPhase.BELOW_HUD));
		event.registerAboveAll("above_overlay", new ApoliPowerOverlay(configuration -> configuration.phase() == OverlayConfiguration.DrawPhase.ABOVE_HUD));
	}
}
