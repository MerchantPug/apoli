package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.ModifyPlayerSpawnConfiguration;
import io.github.apace100.apoli.Apoli;
import net.minecraft.world.entity.LivingEntity;

public class ModifyPlayerSpawnPower extends PowerFactory<ModifyPlayerSpawnConfiguration> {

	public ModifyPlayerSpawnPower() {
		super(ModifyPlayerSpawnConfiguration.CODEC);
	}

	public void teleportToModifiedSpawn(ConfiguredPower<ModifyPlayerSpawnConfiguration, ?> configuration, LivingEntity entity) {
		if (entity instanceof ServerPlayerEntity serverPlayer) {
			Pair<ServerWorld, BlockPos> spawn = configuration.getConfiguration().getSpawn(entity, false);
			if (spawn != null) {
				Vec3d tpPos = Dismounting.findRespawnPos(EntityType.PLAYER, spawn.getLeft(), spawn.getRight(), true);
				if (tpPos != null) {
					serverPlayer.teleport(spawn.getLeft(), tpPos.x, tpPos.y, tpPos.z, entity.getPitch(), entity.getYaw());
				} else {
					serverPlayer.teleport(spawn.getLeft(), spawn.getRight().getX(), spawn.getRight().getY(), spawn.getRight().getZ(), entity.getPitch(), entity.getYaw());
					Apoli.LOGGER.warn("Could not spawn player with `ModifySpawnPower` at the desired location.");
				}
			}
		}
	}

	@Override
	public void onRemoved(ConfiguredPower<ModifyPlayerSpawnConfiguration, ?> configuration, LivingEntity player) {
		if (player instanceof ServerPlayerEntity serverPlayer) {
			if (serverPlayer.getSpawnPointPosition() != null && serverPlayer.isSpawnPointSet())
				serverPlayer.setSpawnPoint(World.OVERWORLD, null, 0F, false, false);
		}
	}
}

