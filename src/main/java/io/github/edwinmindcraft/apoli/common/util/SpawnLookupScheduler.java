package io.github.edwinmindcraft.apoli.common.util;

import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.ApoliCommon;
import io.github.edwinmindcraft.apoli.common.network.S2CCachedSpawnsPacket;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyPlayerSpawnConfiguration;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.*;

public class SpawnLookupScheduler extends Thread {
    private static SpawnLookupScheduler INSTANCE = new SpawnLookupScheduler();

    private final Object2IntMap<ResourceKey<ConfiguredPower<?, ?>>> powers = new Object2IntOpenHashMap<>();
    private final HashSet<ResourceKey<ConfiguredPower<?, ?>>> handled = new HashSet<>();
    private boolean isRunning = false;

    private SpawnLookupScheduler() {}

    /**
     * Registers a power for lookup or increases priority if this power is already being looked up.
     * This should be called when a ModifySpawnPower is chosen, and optionally on world start.
     *
     * @param power The name of the power to increase the priority of.
     */
    public void increasePriority(ResourceKey<ConfiguredPower<?, ?>> power) {
        synchronized (this) {
            if (this.handled.contains(power))
                return;
            this.powers.compute(power, (key, oldValue) -> oldValue != null ? oldValue + 1 : 0);
            if (!this.isRunning) {
                this.isRunning = true;
                this.start();
            }
        }
    }

    /**
     * Marks a position as being invalid, and needing to be looked up again.
     */
    public void invalidate(ResourceKey<ConfiguredPower<?, ?>> power) {
        synchronized (this) {
            this.handled.remove(power);
            // Clear cache position for power.
			SpawnLookupUtil.clearSpawnCacheValue(power);
            ApoliCommon.CHANNEL.send(PacketDistributor.ALL.noArg(), new S2CCachedSpawnsPacket(Set.of(power), true));

            // The following isn't necessary if the we're using lazy loading.
            // Adds the newly invalidated position to the positions to recompute.
            this.powers.compute(power, (key, oldValue) -> oldValue != null ? oldValue : 0);
            if (!this.isRunning) {
                this.isRunning = true;
                this.start();
            }
        }
    }

    /**
     * Removes all lookup data from the thread.
     * This should be called whenever powers are reloaded.
     */
    public void clear() throws InterruptedException {
        // Clears a first time to interrupt any queued actions.
        synchronized (this) {
            this.powers.clear();
            this.handled.clear();
        }
        while (true) {
            boolean flag;
            synchronized(this) {
                flag = this.isRunning;
            }
            if (flag) {
                Thread.sleep(50);
            } else
                break;
        }
        // Clear a second time to make sure the states have been correctly invalidated.
        synchronized (this) {
            this.powers.clear();
            this.handled.clear();
        }
        // Clear cache position for all powers.
		SpawnLookupUtil.resetSpawnCache();
    }

    @Override
    public void run() {
        while (true) {
            ResourceKey<ConfiguredPower<?, ?>> power;
            synchronized (this) {
                // At each step, lookup the most requested power, and treat it. If there is no maximum, max should return a random entry in the map.
                Optional<ResourceKey<ConfiguredPower<?, ?>>> next = this.powers.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey);
                if (next.isEmpty()) {
                    // Keep this in the synchronized block to ensure the thread restarts if it had stopped.
                    this.isRunning = false;
                    break;
                }
                power = next.get();
                this.handled.add(power);
                this.powers.removeInt(power);
            }
            // Basically the only part of the code that is threaded is the spawn lookup.
            doSpawnLookup(power);
        }
    }

    public void doSpawnLookup(ResourceKey<ConfiguredPower<?, ?>> power) {
        ConfiguredPower<?, ?> configuredPower = ApoliAPI.getPowers().get(power);
        if (configuredPower.getConfiguration() instanceof ModifyPlayerSpawnConfiguration configuration) {
            ResourceKey<Level> dimension = configuration.dimension();
            ServerLevel world = ServerLifecycleHooks.getCurrentServer().getLevel(dimension);
            BlockPos regularSpawn = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD)).getSharedSpawnPos();
            BlockPos spawnToDimPos;
            if (world == null) {
                Apoli.LOGGER.warn("Could not find dimension \"{}\".", dimension);
                return;
            }
            int center = world.getLogicalHeight() / 2;
            Vec3 tpPos;
            int range = 64;
            String strategy = configuration.strategy();
            float distanceMultiplier = configuration.distanceMultiplier();
            switch (strategy) {
                case "center" -> spawnToDimPos = new BlockPos(0, center, 0);
                case "default" -> {
                    if (distanceMultiplier != 0) {
                        spawnToDimPos = new BlockPos(regularSpawn.getX() * distanceMultiplier, regularSpawn.getY(), regularSpawn.getZ() * distanceMultiplier);
                    } else {
                        spawnToDimPos = new BlockPos(regularSpawn.getX(), regularSpawn.getY(), regularSpawn.getZ());
                    }
                }
                default -> {
                    Apoli.LOGGER.warn("This case does nothing. The game crashes if there is no spawn strategy set");
                    if (distanceMultiplier != 0) {
                        spawnToDimPos = new BlockPos(regularSpawn.getX() * distanceMultiplier, regularSpawn.getY(), regularSpawn.getZ() * distanceMultiplier);
                    } else {
                        spawnToDimPos = new BlockPos(regularSpawn.getX(), regularSpawn.getY(), regularSpawn.getZ());
                    }
                }
            }
            @Nullable ResourceKey<Biome> biome = configuration.biome();
            if (biome != null) {
                Pair<BlockPos, Holder<Biome>> biomePos = world.findClosestBiome3d(x -> x.is(biome), spawnToDimPos, 6400, 8, 8);
                if (biomePos != null) {
                    spawnToDimPos = biomePos.getFirst();
                } else {
                    Apoli.LOGGER.warn("Could not find biome \"{}\" in dimension \"{}\".", biome, dimension.location());
                }
            }
            @Nullable ResourceKey<Structure> structureKey = configuration.structure();
            if (structureKey == null) {
                tpPos = ModifyPlayerSpawnConfiguration.getValidSpawn(spawnToDimPos, range, world);
            } else {
                Pair<BlockPos, Holder<Structure>> structure = ModifyPlayerSpawnConfiguration.getStructureLocation(world, structureKey, null, dimension);
                ChunkPos structureChunkPos;
                if (structure == null) {
                    Apoli.LOGGER.warn("Could not find structure \"{}\" in dimension \"{}\".", structureKey.location(), dimension.location());
                    tpPos = null;
                } else {
                    BlockPos structurePos = structure.getFirst();
                    structureChunkPos = new ChunkPos(structurePos.getX() >> 4, structurePos.getZ() >> 4);
                    StructureStart structureStart = world.structureManager().getStartForStructure(SectionPos.of(structureChunkPos, 0), structure.getSecond().value(), world.getChunk(structurePos));
                    if (structureStart != null) {
                        BlockPos structureCenter = new BlockPos(structureStart.getBoundingBox().getCenter());
                        tpPos = ModifyPlayerSpawnConfiguration.getValidSpawn(structureCenter, range, world);
                    } else {
                        Apoli.LOGGER.warn("Could not find structure start for \"{}\" in dimension \"{}\".", structure, dimension.toString());
                        tpPos = null;
                    }
                }
            }

            if (tpPos != null) {
                SpawnLookupUtil.changeSpawnCacheValue(power, world, tpPos);
                SpawnLookupUtil.addToPowersWithSpawns(power);
                ApoliCommon.CHANNEL.send(PacketDistributor.ALL.noArg(), new S2CCachedSpawnsPacket(Set.of(power)));
            } else
                handleFailure(power);
        }
    }

    private void handleFailure(ResourceKey<ConfiguredPower<?, ?>> power) {
        SpawnLookupUtil.emptySpawnCacheValue(power);
        SpawnLookupUtil.addToPowersWithSpawns(power);
        ApoliCommon.CHANNEL.send(PacketDistributor.ALL.noArg(), new S2CCachedSpawnsPacket(Set.of(power)));
    }
}