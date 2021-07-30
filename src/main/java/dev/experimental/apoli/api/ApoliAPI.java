package dev.experimental.apoli.api;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.registry.ApoliDynamicRegistries;
import dev.experimental.calio.api.CalioAPI;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class ApoliAPI {
	public static final String MODID = "apoli";

	public static IPowerContainer getPowerContainer(Entity entity) {
		throw new AssertionError();
	}

	public static boolean hasFoodRestrictions() {
		//FIXME Support for disabling food restrictions.
		return true;
	}

	public static void synchronizePowerContainer(LivingEntity living) {
		throw new AssertionError();
	}

	public static ResourceLocation identifier(String path) {
		return new ResourceLocation(MODID, path);
	}

	/**
	 * An accessor for the {@link ConfiguredPower} registry.
	 * You should probably cache this value if you can.
	 *
	 * @param server The server to get the registry for, or null for the client.
	 *
	 * @return The ConfiguredPower registry.
	 */
	public static Registry<ConfiguredPower<?, ?>> getPowers(@Nullable MinecraftServer server) {
		return CalioAPI.getDynamicRegistries(server).get(ApoliDynamicRegistries.CONFIGURED_POWER_KEY);
	}

	/**
	 * An accessor for the {@link ConfiguredPower} registry.
	 * You should probably cache this value if you can.
	 *
	 * @return The ConfiguredPower registry.
	 */
	public static Registry<ConfiguredPower<?, ?>> getPowers() {
		return CalioAPI.getDynamicRegistries().get(ApoliDynamicRegistries.CONFIGURED_POWER_KEY);
	}
}
