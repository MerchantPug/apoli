package io.github.edwinmindcraft.apoli.common.util;

import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.registry.ApoliDynamicRegistries;
import io.github.edwinmindcraft.apoli.common.ApoliCommon;
import io.github.edwinmindcraft.apoli.common.network.S2CCachedSpawnsPacket;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyPlayerSpawnConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
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

public class SpawnSearchInstance {
    private final ConfiguredPower<ModifyPlayerSpawnConfiguration, ?> configuration;
    private final Optional<ResourceLocation> powerId;

    public SpawnSearchInstance(ConfiguredPower<ModifyPlayerSpawnConfiguration, ?> configuration, Optional<ResourceLocation> powerId) {
        this.configuration = configuration;
        this.powerId = powerId;
    }

    private static final Set<ResourceKey<ConfiguredPower<?, ?>>> POWERS_WITH_SPAWNS = Collections.synchronizedSet(new HashSet<>());
    private static final Map<ResourceKey<ConfiguredPower<?, ?>>, Tuple<ServerLevel, Vec3>> SPAWN_CACHE = Collections.synchronizedMap(new HashMap<>());

    public static Set<ResourceKey<ConfiguredPower<?, ?>>> getPowersWithSpawns() {
        return Set.copyOf(POWERS_WITH_SPAWNS);
    }

    public static boolean hasSpawnCached(ResourceKey<ConfiguredPower<?, ?>> key) {
        return POWERS_WITH_SPAWNS.contains(key);
    }

    public static Tuple<ServerLevel, Vec3> getSpawnCache(ResourceKey<ConfiguredPower<?, ?>> key) {
        return SPAWN_CACHE.get(key);
    }

    public static void addToPowersWithSpawns(ResourceKey<ConfiguredPower<?, ?>> key) {
        POWERS_WITH_SPAWNS.add(key);
    }

    public static void changeSpawnCacheValue(ResourceKey<ConfiguredPower<?, ?>> key, ServerLevel level, Vec3 vec3) {
        SPAWN_CACHE.put(key, new Tuple<>(level, vec3));
    }

    public static void resetSpawnCache() {
        POWERS_WITH_SPAWNS.clear();
        SPAWN_CACHE.clear();
    }

    public void run() {
        ResourceKey<Level> dimension = configuration.getConfiguration().dimension();
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
        String strategy = configuration.getConfiguration().strategy();
        float distanceMultiplier = configuration.getConfiguration().distanceMultiplier();
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
        @Nullable ResourceKey<Biome> biome = configuration.getConfiguration().biome();
        if (biome != null) {
            Pair<BlockPos, Holder<Biome>> biomePos = world.findClosestBiome3d(x -> x.is(biome), spawnToDimPos, 6400, 8, 8);
            if (biomePos != null) {
                spawnToDimPos = biomePos.getFirst();
            } else {
                Apoli.LOGGER.warn("Could not find biome \"{}\" in dimension \"{}\".", biome, dimension.toString());
            }
        }
        @Nullable ResourceKey<Structure> structureKey = configuration.getConfiguration().structure();
        if (structureKey == null) {
            tpPos = configuration.getConfiguration().getValidSpawn(spawnToDimPos, range, world);
        } else {
            Pair<BlockPos, Holder<Structure>> structure = configuration.getConfiguration().getStructureLocation(world, structureKey, null, dimension);
            ChunkPos structureChunkPos;
            if (structure == null) {
                Apoli.LOGGER.warn("Could not find structure \"{}\" in dimension \"{}\".", structure, dimension.toString());
                return;
            }
            BlockPos structurePos = structure.getFirst();
            structureChunkPos = new ChunkPos(structurePos.getX() >> 4, structurePos.getZ() >> 4);
            StructureStart structureStart = world.structureManager().getStartForStructure(SectionPos.of(structureChunkPos, 0), structure.getSecond().value(), world.getChunk(structurePos));
            if (structureStart != null) {
                BlockPos structureCenter = new BlockPos(structureStart.getBoundingBox().getCenter());
                tpPos = configuration.getConfiguration().getValidSpawn(structureCenter, range, world);
            } else
                tpPos = null;
        }

        if (tpPos != null) {
            ResourceKey<ConfiguredPower<?, ?>> power = ResourceKey.create(ApoliDynamicRegistries.CONFIGURED_POWER_KEY, powerId.orElseGet(configuration::getRegistryName));
            changeSpawnCacheValue(power, world, tpPos);
            addToPowersWithSpawns(power);
            ApoliCommon.CHANNEL.send(PacketDistributor.ALL.noArg(), new S2CCachedSpawnsPacket(Set.of(power)));
        }
    }
}