package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import io.github.apace100.calio.data.SerializableDataType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import java.util.Objects;

public class GameModeCondition extends EntityCondition<FieldConfiguration<GameType>> {

	public GameModeCondition() {
		super(FieldConfiguration.codec(SerializableDataType.enumValue(GameType.class), "gamemode"));
	}

	protected boolean testClient(GameType mode, LivingEntity entity) {
		return false;
	}

	@Override
	public boolean check(FieldConfiguration<GameType> configuration, LivingEntity entity) {
		if (entity instanceof ServerPlayer) {
			ServerPlayerGameMode interactionMngr = ((ServerPlayer) entity).gameMode;
			return Objects.equals(interactionMngr.getGameModeForPlayer(), configuration.value());
		}
		return testClient(configuration.value(), entity);
	}

	@Environment(EnvType.CLIENT)
	public static class Client extends GameModeCondition {
		public Client() {
			super();
		}

		@Override
		protected boolean testClient(GameType mode, LivingEntity entity) {
			return entity instanceof LocalPlayer && Objects.equals(Minecraft.getInstance().gameMode.getPlayerMode(), mode);
		}
	}
}
