package dev.experimental.apoli.client;

import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.common.power.InvisibilityPower;
import dev.experimental.apoli.common.registry.ModPowers;
import io.github.apace100.apoli.Apoli;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Apoli.MODID, value = Dist.CLIENT)
public class ApoliClientEventHandler {

	private static HashMap<String, KeyMapping> idToKeyBindingMap = new HashMap<>();
	private static HashMap<String, Boolean> lastKeyBindingStates = new HashMap<>();
	private static boolean initializedKeyBindingMap = false;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onBlockOverlay(RenderBlockOverlayEvent event) {
		if (event.getPlayer() != null) {
			if (IPowerContainer.hasPower(event.getPlayer(), ModPowers.PHASING.get()))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLivingRender(RenderLivingEvent.Pre<LivingEntity, ?> event) {
		//TODO Check if this is fired correctly.
		if (InvisibilityPower.isArmorHidden(event.getEntity()))
			event.setCanceled(true);
	}

	private static KeyMapping getKeyBinding(String key) {
		if (!idToKeyBindingMap.containsKey(key)) {
			if (!initializedKeyBindingMap) {
				initializedKeyBindingMap = true;
				Minecraft client = Minecraft.getInstance();
				for (int i = 0; i < client.options.keyMappings.length; i++) {
					idToKeyBindingMap.put(client.options.keyMappings[i].getName(), client.options.keyMappings[i]);
				}
				return getKeyBinding(key);
			}
			return null;
		}
		return idToKeyBindingMap.get(key);
	}

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			Minecraft instance = Minecraft.getInstance();
			LocalPlayer player = instance.player;
			if (player != null) {
				IPowerContainer.get(player).ifPresent(container -> {
					HashMap<String, Boolean> currentKeyBindingStates = new HashMap<>();
					Set<ResourceLocation> pressedPowers = new HashSet<>();
					Registry<ConfiguredPower<?, ?>> powers = ApoliAPI.getPowers();
					for (ConfiguredPower<?, ?> power : container.getPowers()) {
						power.getKey(player).ifPresent(key -> {
							KeyMapping binding = getKeyBinding(key.key());
							if (binding != null) {
								if (!currentKeyBindingStates.containsKey(key.key()))
									currentKeyBindingStates.put(key.key(), binding.isDown());
								if (currentKeyBindingStates.get(key.key()) && (key.continuous() || !lastKeyBindingStates.getOrDefault(key.key(), false)))
									pressedPowers.add(powers.getKey(power));
							}
						});
					}
					lastKeyBindingStates = currentKeyBindingStates;
					if (pressedPowers.size() > 0) {
						ApoliAPI.performPowers(pressedPowers);
					}
				});
			}
		}
	}

}
