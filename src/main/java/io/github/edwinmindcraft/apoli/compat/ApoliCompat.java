package io.github.edwinmindcraft.apoli.compat;

import io.github.edwinmindcraft.apoli.compat.citadel.CitadelEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

public class ApoliCompat {
	public static void apply() {
		if (ModList.get().isLoaded("citadel")) applyCitadelCompat();
	}

	private static void applyCitadelCompat() {
		MinecraftForge.EVENT_BUS.register(new CitadelEventHandler());
	}
}
