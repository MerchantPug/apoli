package io.github.edwinmindcraft.apoli.client.screen;

import io.github.edwinmindcraft.apoli.common.power.configuration.OverlayConfiguration;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;

public class ApoliOverlays {
	//FIXME Looking at the code, a forge PR might be needed to not hide the gui with the HUD.
	public static final IIngameOverlay APOLI_OVERLAY = OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Apoli Overlays", ApoliOverlay.INSTANCE);
	public static final IIngameOverlay APOLI_BOTTOM_OVERLAY = OverlayRegistry.registerOverlayBottom("Apoli Power Overlay (Below)", new ApoliPowerOverlay(configuration -> configuration.phase() == OverlayConfiguration.DrawPhase.BELOW_HUD));
	public static final IIngameOverlay APOLI_TOP_OVERLAY = OverlayRegistry.registerOverlayBottom("Apoli Power Overlay (Above)", new ApoliPowerOverlay(configuration -> configuration.phase() == OverlayConfiguration.DrawPhase.ABOVE_HUD));

	public static void bootstrap() {

	}
}
