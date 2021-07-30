package io.github.apace100.apoli.power;

import io.github.apace100.apoli.Apoli;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;

public class ModifyPlayerSpawnPower extends Power {
    public final ResourceKey<Level> dimension;
    public final float dimensionDistanceMultiplier;
    public final ResourceLocation biomeId;
    public final String spawnStrategy;
    public final StructureFeature structure;
    public final SoundEvent spawnSound;

    public ModifyPlayerSpawnPower(PowerType<?> type, LivingEntity entity, ResourceKey<Level> dimension, float dimensionDistanceMultiplier, ResourceLocation biomeId, String spawnStrategy, StructureFeature<?> structure, SoundEvent spawnSound) {
        super(type, entity);
        this.dimension = dimension;
        this.dimensionDistanceMultiplier = dimensionDistanceMultiplier;
        this.biomeId = biomeId;
        this.spawnStrategy = spawnStrategy;
        this.structure = structure;
        this.spawnSound = spawnSound;
    }

    public void teleportToModifiedSpawn() {
        if(entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) entity;
            Tuple<ServerLevel, BlockPos> spawn = getSpawn(false);
            if(spawn != null) {
                Vec3 tpPos = DismountHelper.findSafeDismountLocation(EntityType.PLAYER, spawn.getA(), spawn.getB(), true);
                if(tpPos != null) {
                    serverPlayer.teleportTo(spawn.getA(), tpPos.x, tpPos.y, tpPos.z, entity.getXRot(), entity.getYRot());
                } else {
                    serverPlayer.teleportTo(spawn.getA(), spawn.getB().getX(), spawn.getB().getY(), spawn.getB().getZ(), entity.getXRot(), entity.getYRot());
                    Apoli.LOGGER.warn("Could not spawn player with `ModifySpawnPower` at the desired location.");
                }
            }
        }
    }

    @Override
    public void onRemoved() {
        if(entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) entity;
            if(serverPlayer.getRespawnPosition() != null && serverPlayer.isRespawnForced()) {
                serverPlayer.setRespawnPosition(Level.OVERWORLD, null, 0F, false, false);
            }
        }
    }

    public Tuple<ServerLevel, BlockPos> getSpawn(boolean isSpawnObstructed) {
        if(entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) entity;
            ServerLevel world = serverPlayer.getLevel().getServer().getLevel(dimension);
            BlockPos regularSpawn = serverPlayer.getLevel().getServer().getLevel(Level.OVERWORLD).getSharedSpawnPos();
            BlockPos spawnToDimPos;
            int iterations = (world.getLogicalHeight() / 2) - 8;
            int center = world.getLogicalHeight() / 2;
            BlockPos.MutableBlockPos mutable;
            Vec3 tpPos;
            int range = 64;

            switch(spawnStrategy) {
                case "center":
                    spawnToDimPos = new BlockPos(0, center, 0);
                    break;

                case "default":
                    if(dimensionDistanceMultiplier != 0) {
                        spawnToDimPos = new BlockPos(regularSpawn.getX() * dimensionDistanceMultiplier, regularSpawn.getY(), regularSpawn.getZ() * dimensionDistanceMultiplier);
                    } else {
                        spawnToDimPos = new BlockPos(regularSpawn.getX(), regularSpawn.getY(), regularSpawn.getZ());
                    }
                    break;

                default:
                    Apoli.LOGGER.warn("This case does nothing. The game crashes if there is no spawn strategy set");
                    if(dimensionDistanceMultiplier != 0) {
                        spawnToDimPos = new BlockPos(regularSpawn.getX() * dimensionDistanceMultiplier, regularSpawn.getY(), regularSpawn.getZ() * dimensionDistanceMultiplier);
                    } else {
                        spawnToDimPos = new BlockPos(regularSpawn.getX(), regularSpawn.getY(), regularSpawn.getZ());
                    }
            }

            if(biomeId != null) {
                Optional<Biome> biomeOptional = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getOptional(biomeId);
                if(biomeOptional.isPresent()) {
                    BlockPos biomePos = world.findNearestBiome(biomeOptional.get(), spawnToDimPos, 6400, 8);
                    if(biomePos != null) {
                        spawnToDimPos = biomePos;
                    } else {
                        Apoli.LOGGER.warn("Could not find biome \"" + biomeId.toString() + "\" in dimension \"" + dimension.toString() + "\".");
                    }
                } else {
                    Apoli.LOGGER.warn("Biome with ID \"" + biomeId.toString() + "\" was not registered.");
                }
            }

            if(structure == null) {
                tpPos = getValidSpawn(spawnToDimPos, range, world);
            } else {
                BlockPos structurePos = getStructureLocation(structure, dimension);
                ChunkPos structureChunkPos;

                if(structurePos == null) {
                    return null;
                }
                structureChunkPos = new ChunkPos(structurePos.getX() >> 4, structurePos.getZ() >> 4);
                StructureStart structureStart = world.structureFeatureManager().getStartForFeature(SectionPos.of(structureChunkPos, 0), structure, world.getChunk(structurePos));
                BlockPos structureCenter = new BlockPos(structureStart.getBoundingBox().getCenter());
                tpPos = getValidSpawn(structureCenter, range, world);
            }

            if(tpPos != null) {
                mutable = new BlockPos(tpPos.x, tpPos.y, tpPos.z).mutable();
                BlockPos spawnLocation = mutable;
                world.getChunkSource().addRegionTicket(TicketType.START, new ChunkPos(spawnLocation), 11, Unit.INSTANCE);
                return new Tuple(world, spawnLocation);
            }
            return null;
        }
        return null;
    }

    private BlockPos getStructureLocation(StructureFeature structure, ResourceKey<Level> dimension) {
        BlockPos blockPos = new BlockPos(0, 70, 0);
        ServerLevel serverWorld = entity.getServer().getLevel(dimension);
        BlockPos blockPos2 = serverWorld.findNearestMapFeature(structure, blockPos, 100, false);
        //FrostburnOrigins.LOGGER.warn("Unrecognized dimension id '" + dimensionId + "', defaulting to id '0', OVERWORLD");
        if (blockPos2 == null) {
            Apoli.LOGGER.warn("Could not find '" + structure.getFeatureName() + "' in dimension: " + dimension.location());
            return null;
        } else {
            return blockPos2;
        }
    }

    private Vec3 getValidSpawn(BlockPos startPos, int range, ServerLevel world) {
        // (di, dj) is a vector - direction in which we move right now
        int dx = 1;
        int dz = 0;
        // length of current segment
        int segmentLength = 1;
        BlockPos.MutableBlockPos mutable = startPos.mutable();
        // center of our starting structure, or dimension
        int center = startPos.getY();
        // Our valid spawn location
        Vec3 tpPos;

        // current position (x, z) and how much of current segment we passed
        int x = startPos.getX();
        int z = startPos.getZ();
        //position to check up, or down
        int segmentPassed = 0;
        // increase y check
        int i = 0;
        // Decrease y check
        int d = 0;
        while(i < world.getLogicalHeight() || d > 0) {
            for (int coordinateCount = 0; coordinateCount < range; ++coordinateCount) {
                // make a step, add 'direction' vector (di, dj) to current position (i, j)
                x += dx;
                z += dz;
                ++segmentPassed;mutable.setX(x);
                mutable.setZ(z);
                mutable.setY(center + i);
                tpPos = DismountHelper.findSafeDismountLocation(EntityType.PLAYER, world, mutable, true);
                if (tpPos != null) {
                    return(tpPos);
                } else {
                    mutable.setY(center + d);
                    tpPos = DismountHelper.findSafeDismountLocation(EntityType.PLAYER, world, mutable, true);
                    if (tpPos != null) {
                        return(tpPos);
                    }
                }

                if (segmentPassed == segmentLength) {
                    // done with current segment
                    segmentPassed = 0;

                    // 'rotate' directions
                    int buffer = dx;
                    dx = -dz;
                    dz = buffer;

                    // increase segment length if necessary
                    if (dz == 0) {
                        ++segmentLength;
                    }
                }
            }
            i++;
            d--;
        }
        return(null);
    }
}

