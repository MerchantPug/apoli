package io.github.apace100.apoli;

import io.github.apace100.apoli.power.factory.condition.EntityConditionsClient;
import io.github.apace100.apoli.power.factory.condition.ItemConditionsClient;
import io.github.apace100.apoli.registry.ApoliClassDataClient;
import io.github.apace100.apoli.screen.GameHudRender;
import io.github.apace100.apoli.screen.PowerHudRenderer;
import io.github.edwinmindcraft.apoli.client.ApoliClientEventHandler;
import io.github.edwinmindcraft.apoli.client.screen.ApoliOverlay;
import io.github.edwinmindcraft.apoli.client.screen.ApoliOverlays;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
public class ApoliClient {

	public static boolean shouldReloadWorldRenderer = false;
	public static boolean shouldReapplyShaders = false;

	public static void registerPowerKeybinding(String keyId, KeyMapping keyBinding) {
		ApoliClientEventHandler.registerPowerKeybinding(keyId, keyBinding);
	}

	public static void initialize() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(ApoliClient::setupClient);
		modEventBus.addListener(ApoliOverlays::registerOverlays);

		ApoliClassDataClient.registerAll();

		EntityConditionsClient.register();
		ItemConditionsClient.register();
		GameHudRender.HUD_RENDERS.add(new PowerHudRenderer());
	}

	public static void setupClient(FMLClientSetupEvent event) {
		ApoliOverlays.bootstrap();
	}
}
