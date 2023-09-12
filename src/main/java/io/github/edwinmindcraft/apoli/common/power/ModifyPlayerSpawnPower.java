package io.github.edwinmindcraft.apoli.common.power;

import com.google.common.collect.Sets;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.util.ApoliConfigs;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliDynamicRegistries;
import io.github.edwinmindcraft.apoli.common.ApoliCommon;
import io.github.edwinmindcraft.apoli.common.network.S2CActiveSpawnPowerPacket;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyPlayerSpawnConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import io.github.edwinmindcraft.apoli.common.util.ModifyPlayerSpawnCache;
import io.github.edwinmindcraft.apoli.common.util.SpawnSearchInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Tuple;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModifyPlayerSpawnPower extends PowerFactory<ModifyPlayerSpawnConfiguration> {

	public ModifyPlayerSpawnPower() {
		super(ModifyPlayerSpawnConfiguration.CODEC);
	}

	public void teleportToModifiedSpawn(ConfiguredPower<?, ?> configuration, Entity entity) {
		if (entity instanceof ServerPlayer serverPlayer) {
			Tuple<ResourceKey<ConfiguredPower<?, ?>>, Tuple<ServerLevel, Vec3>> cachedSpawn = getSpawn(configuration, entity, false);
			if (cachedSpawn != null) {
                Tuple<ServerLevel, Vec3> spawn = cachedSpawn.getB();
                BlockPos spawnLocation = new BlockPos(spawn.getB().x, spawn.getB().y, spawn.getB().z).mutable();
                spawn.getA().getChunkSource().addRegionTicket(TicketType.START, new ChunkPos(spawnLocation), 11, Unit.INSTANCE);

				Vec3 tpPos = DismountHelper.findSafeDismountLocation(EntityType.PLAYER, spawn.getA(), new BlockPos(spawn.getB()), true);
				if (tpPos != null) {
					serverPlayer.teleportTo(spawn.getA(), tpPos.x, tpPos.y, tpPos.z, entity.getXRot(), entity.getYRot());
                    if (tpPos != spawn.getB()) {
                        SpawnSearchInstance.changeSpawnCacheValue(cachedSpawn.getA(), spawn.getA(), tpPos);
                    }
				} else {
					serverPlayer.teleportTo(spawn.getA(), spawn.getB().x(), spawn.getB().y(), spawn.getB().z(), entity.getXRot(), entity.getYRot());
					Apoli.LOGGER.warn("Could not spawn player with `ModifySpawnPower` at the desired location.");
				}
			}
		}
	}

    @Nullable
    public Tuple<ResourceKey<ConfiguredPower<?, ?>>, Tuple<ServerLevel, Vec3>> getSpawn(Optional<ConfiguredPower<ModifyPlayerSpawnConfiguration, ModifyPlayerSpawnPower>> configuration, Entity entity, boolean sendToClient) {
        if (configuration.isPresent())
            return getSpawn(configuration.get(), entity, sendToClient);
        else {
            ((ModifyPlayerSpawnCache)entity).removeActiveSpawnPower();
            return null;
        }
    }

    @Nullable
    public Tuple<ResourceKey<ConfiguredPower<?, ?>>, Tuple<ServerLevel, Vec3>> getSpawn(ConfiguredPower<?, ?> configuration, Entity entity, boolean sendToClient) {
        if (entity instanceof ServerPlayer serverPlayer) {
            @Nullable ResourceKey<ConfiguredPower<?, ?>> otherKey = ((ModifyPlayerSpawnCache)entity).getActiveSpawnPower();
            if (otherKey != null && SpawnSearchInstance.getSpawnCache(otherKey) != null && ApoliAPI.getPowerContainer(entity).hasPower(otherKey))
                return new Tuple<>(otherKey, SpawnSearchInstance.getSpawnCache(otherKey));

            ResourceKey<ConfiguredPower<?, ?>> key = ResourceKey.create(ApoliDynamicRegistries.CONFIGURED_POWER_KEY, configuration.getRegistryName());
            ((ModifyPlayerSpawnCache)serverPlayer).setActiveSpawnPower(key);
            if (sendToClient)
                ApoliCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new S2CActiveSpawnPowerPacket(key));

            if (SpawnSearchInstance.getSpawnCache(key) != null) {
                return new Tuple<>(key, SpawnSearchInstance.getSpawnCache(key));
            }
        }
        return null;
    }


    @Override
    public void onAdded(ConfiguredPower<ModifyPlayerSpawnConfiguration, ?> configuration, Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer && ((ModifyPlayerSpawnCache)entity).getActiveSpawnPower() == null) {
            getSpawn(configuration, entity, serverPlayer.isDeadOrDying());
        }
    }


    @Override
    public void onRemoved(ConfiguredPower<ModifyPlayerSpawnConfiguration, ?> configuration, Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer)
            if (!serverPlayer.hasDisconnected() && serverPlayer.getRespawnPosition() != null && serverPlayer.isRespawnForced())
                serverPlayer.setRespawnPosition(Level.OVERWORLD, null, 0F, false, false);
    }

    // This is not the same tick method because we'd prefer it to only run once.
    public void tick(Entity entity) {
        if (entity instanceof ModifyPlayerSpawnCache cache && entity instanceof ServerPlayer spe && cache.getActiveSpawnPower() != null) {
            IPowerContainer.get(entity).ifPresent(container -> {
                if ((!container.hasPower(cache.getActiveSpawnPower()) || !container.getPower(cache.getActiveSpawnPower()).isBound() || ApoliAPI.getPowers().containsKey(cache.getActiveSpawnPower()) && !ApoliAPI.getPowers().get(cache.getActiveSpawnPower()).isConfigurationValid() || ApoliAPI.getPowers().containsKey(cache.getActiveSpawnPower()) && !ApoliAPI.getPowers().get(cache.getActiveSpawnPower()).isActive(entity))) {
                    Optional<ConfiguredPower<ModifyPlayerSpawnConfiguration, ModifyPlayerSpawnPower>> optional = container.getPowers(ApoliPowers.MODIFY_PLAYER_SPAWN.get()).stream().filter(Holder::isBound).map(Holder::value).findFirst();
                    getSpawn(optional, entity, spe.isDeadOrDying());
                }
            });
        }
    }

    private static final Set<ServerPlayer> PLAYERS_TO_RESPAWN = Sets.newHashSet();

    public void schedulePlayerToRespawn(ServerPlayer player) {
        PLAYERS_TO_RESPAWN.add(player);
    }

	@Override
	public void onRespawn(ConfiguredPower<ModifyPlayerSpawnConfiguration, ?> configuration, Entity entity) {
		if (entity instanceof ServerPlayer player && PLAYERS_TO_RESPAWN.contains(player) && player.getRespawnPosition() == null) {
            this.teleportToModifiedSpawn(configuration, entity);
            PLAYERS_TO_RESPAWN.remove(player);
        }
	}

    private static final ExecutorService FINDER_POOL = Executors.newFixedThreadPool(1);

    public void findSpawn(ConfiguredPower<ModifyPlayerSpawnConfiguration, ?> configuration, @Nullable ResourceLocation powerId) {
        SpawnSearchInstance searchThread = new SpawnSearchInstance(configuration, Optional.ofNullable(powerId));
        if (ApoliConfigs.SERVER.separateSpawnFindingThread.get())
            FINDER_POOL.submit(searchThread::run);
        else
            searchThread.run();
    }
}

