package io.github.edwinmindcraft.apoli.api;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.registry.ApoliDynamicRegistries;
import io.github.edwinmindcraft.apoli.common.ApoliCommon;
import io.github.edwinmindcraft.apoli.common.network.S2CSynchronizePowerContainer;
import io.github.edwinmindcraft.calio.api.CalioAPI;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ApoliAPI {
	public static final String MODID = "apoli";

	@Nullable
	public static IPowerContainer getPowerContainer(Entity entity) {
		if (entity instanceof LivingEntity living) {
			LazyOptional<IPowerContainer> optional = IPowerContainer.get(living);
			if (optional.isPresent())
				return optional.orElseThrow(RuntimeException::new);
		}
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public static void performPowers(Set<ResourceLocation> powers) {

	}

	public static boolean hasFoodRestrictions() {
		//FIXME Support for disabling food restrictions.
		return true;
	}

	public static void synchronizePowerContainer(LivingEntity living) {
		S2CSynchronizePowerContainer packet = S2CSynchronizePowerContainer.forEntity(living);
		if (packet != null)
			ApoliCommon.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> living), packet);
	}

	public static void synchronizePowerContainer(LivingEntity living, ServerPlayer with) {
		S2CSynchronizePowerContainer packet = S2CSynchronizePowerContainer.forEntity(living);
		if (packet != null)
			ApoliCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> with), packet);
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
