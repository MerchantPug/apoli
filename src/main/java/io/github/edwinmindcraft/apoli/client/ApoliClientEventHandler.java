package io.github.edwinmindcraft.apoli.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.ApoliClient;
import io.github.apace100.calio.Calio;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.power.InvisibilityPower;
import io.github.edwinmindcraft.apoli.common.power.ParticlePower;
import io.github.edwinmindcraft.apoli.common.power.PhasingPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.PhasingConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import io.github.edwinmindcraft.apoli.common.util.CoreUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Apoli.MODID, value = Dist.CLIENT)
public class ApoliClientEventHandler {

	public static void registerPowerKeybinding(String keyId, KeyMapping keyBinding) {
		idToKeyBindingMap.put(keyId, keyBinding);
	}

	private static final HashMap<String, KeyMapping> idToKeyBindingMap = new HashMap<>();
	private static final HashMap<String, Boolean> lastKeyBindingStates = new HashMap<>();
	private static boolean initializedKeyBindingMap = false;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onBlockOverlay(RenderBlockOverlayEvent event) {
		if (event.getPlayer() != null) {
			if (IPowerContainer.hasPower(event.getPlayer(), ApoliPowers.PHASING.get()))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLivingRender(RenderLivingEvent.Pre<LivingEntity, ?> event) {
		//FIXME Doesn't seem to work with armor.
		if (InvisibilityPower.isArmorHidden(event.getEntity()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingUpdateEvent event) {
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			boolean firstPerson = Minecraft.getInstance().options.getCameraType().isFirstPerson();
			if (!event.getEntity().isInvisibleTo(player)) {
				ParticlePower.renderParticles(event.getEntity(), player, firstPerson);
			}
		}
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
			if (ApoliClient.shouldReapplyShaders) {
				ApoliClient.shouldReapplyShaders = false;
				instance.gameRenderer.checkEntityPostEffect(instance.options.getCameraType().isFirstPerson() ? instance.getCameraEntity() : null);
			}
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
							} else if (Calio.isDebugMode())
								Apoli.LOGGER.warn("No such key: {}", key.key());
						});
					}
					lastKeyBindingStates.clear();
					lastKeyBindingStates.putAll(currentKeyBindingStates);
					if (pressedPowers.size() > 0) {
						ApoliAPI.performPowers(pressedPowers);
					}
				});
			}
		}
	}

	//Replaces redirectFogStart & redirectFogEnd in BackgroundRendererMixin
	@SubscribeEvent
	public static void renderFog(EntityViewRenderEvent.RenderFogEvent event) {
		float start = RenderSystem.getShaderFogStart();
		float end = RenderSystem.getShaderFogEnd();
		FogRenderer.FogMode mode = event.getMode();
		if (event.getCamera().getEntity() instanceof LivingEntity living) {
			Optional<Float> renderMethod = PhasingPower.getRenderMethod(living, PhasingConfiguration.RenderType.BLINDNESS);
			if (renderMethod.isPresent() && CoreUtils.getInWallBlockState(living) != null) {
				float view = renderMethod.get();
				float s;
				float v;
				if (mode == FogRenderer.FogMode.FOG_SKY) {
					s = Math.min(0F, start);
					v = Math.min(view * 0.8F, end);
				} else {
					s = Math.min(view * 0.25F, start);
					v = Math.min(view, end);
				}
				RenderSystem.setShaderFogStart(s);
				RenderSystem.setShaderFogEnd(v);
			}
		}
	}

	//Replaces modifyD in BackgroundRendererMixin
	@SubscribeEvent
	public static void fogColor(EntityViewRenderEvent.FogColors event) {
		if (event.getCamera().getEntity() instanceof LivingEntity living && PhasingPower.hasRenderMethod(living, PhasingConfiguration.RenderType.BLINDNESS) && CoreUtils.getInWallBlockState(living) != null) {
			event.setBlue(0.0F);
			event.setGreen(0.0F);
			event.setRed(0.0F);
		}
	}
}
