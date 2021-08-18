package dev.experimental.apoli.common;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.component.IPowerDataCache;
import dev.experimental.apoli.common.component.PowerContainer;
import dev.experimental.apoli.common.component.PowerDataCache;
import dev.experimental.apoli.common.network.S2CSynchronizePowerContainer;
import dev.experimental.apoli.common.registry.ApoliCapabilities;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.command.PowerCommand;
import io.github.apace100.apoli.command.ResourceCommand;
import io.github.apace100.apoli.data.PowerLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

/**
 * This class contains events that relate to non-power stuff, as in
 * synchronization & capability associations.
 */
@Mod.EventBusSubscriber(modid = Apoli.MODID)
public class ApoliEventHandler {

	public static void attachData(AddReloadListenerEvent event) {
		event.addListener(new PowerLoader());
	}

	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity living) {
			event.addCapability(IPowerContainer.KEY, new PowerContainer(living));
			event.addCapability(IPowerDataCache.KEY, new PowerDataCache());
		}
	}

	@SubscribeEvent
	public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getPlayer() instanceof ServerPlayer spe) {
			S2CSynchronizePowerContainer packet = S2CSynchronizePowerContainer.forEntity(spe);
			if (packet == null)
				Apoli.LOGGER.error("Couldn't create synchronization packet for player {}", spe.getScoreboardName());
			ApoliCommon.CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
		}
	}

	@SubscribeEvent
	public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		IPowerContainer.get(event.getEntityLiving()).ifPresent(x -> x.getPowers().forEach(y -> y.onRemoved(event.getEntityLiving())));
	}

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
			IPowerContainer.get(event.player).ifPresent(IPowerContainer::serverTick);
			//if ((event.player.age & 0x7F) == 0 && event.player instanceof ServerPlayerEntity)
			//	ModComponentsArchitectury.syncWith((ServerPlayerEntity) event.player, event.player);
		}
	}

	@SubscribeEvent
	public static void playerClone(PlayerEvent.Clone event) {
		event.getPlayer().getCapability(ApoliCapabilities.POWER_CONTAINER)
				.ifPresent(target -> event.getOriginal().getCapability(ApoliCapabilities.POWER_CONTAINER)
						.ifPresent(source -> target.readFromNbt(source.writeToNbt(new CompoundTag()))));
		IPowerContainer.get(event.getOriginal()).ifPresent(x -> x.getPowers().forEach(y -> y.onRemoved(event.getOriginal())));
	}

	@SubscribeEvent
	public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getPlayer() instanceof ServerPlayer sp) {
			IPowerContainer.sync(sp);
			if (!event.isEndConquered())
				IPowerContainer.get(sp).ifPresent(x -> x.getPowers().forEach(y -> y.onRespawn(sp)));
		}
	}

	@SubscribeEvent
	public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getPlayer() instanceof ServerPlayer)
			IPowerContainer.sync(event.getPlayer());
	}

	@SubscribeEvent
	public static void trackNew(EntityJoinWorldEvent event) {
		if (event.getWorld().isClientSide())
			return;
		if (event.getEntity() instanceof LivingEntity le)
			IPowerContainer.sync(le);
	}

	@SubscribeEvent
	public static void trackEntity(PlayerEvent.StartTracking event) {
		if (event.getPlayer() instanceof ServerPlayer se && event.getTarget() instanceof LivingEntity target)
			IPowerContainer.sync(target, se);
	}

	@SubscribeEvent
	public static void initializeCommands(RegisterCommandsEvent event) {
		PowerCommand.register(event.getDispatcher());
		ResourceCommand.register(event.getDispatcher());
	}
}
