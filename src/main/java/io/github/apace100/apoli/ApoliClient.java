package io.github.apace100.apoli;

import io.github.edwinmindcraft.apoli.client.ApoliClientEventHandler;
import net.minecraft.client.KeyMapping;

public class ApoliClient {
	public static void registerPowerKeybinding(String keyId, KeyMapping keyBinding) {
		ApoliClientEventHandler.registerPowerKeybinding(keyId, keyBinding);
	}
}
