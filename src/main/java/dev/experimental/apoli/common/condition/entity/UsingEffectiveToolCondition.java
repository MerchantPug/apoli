package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.configuration.NoConfiguration;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UsingEffectiveToolCondition extends EntityCondition<NoConfiguration> {

	public UsingEffectiveToolCondition() {
		super(NoConfiguration.CODEC);
	}

	protected boolean checkClient(LivingEntity entity) {
		return false;
	}

	@Override
	public boolean check(NoConfiguration configuration, LivingEntity entity) {
		if (entity instanceof ServerPlayer spe) {
			ServerPlayerGameMode interactionMngr = spe.gameMode;
			if (interactionMngr.isDestroyingBlock) {
				return spe.hasCorrectToolForDrops(entity.level.getBlockState(interactionMngr.destroyPos));
			}
		}
		return this.checkClient(entity);
	}

	@OnlyIn(Dist.CLIENT)
	public static class Client extends UsingEffectiveToolCondition {
		@Override
		protected boolean checkClient(LivingEntity entity) {
			if (entity instanceof AbstractClientPlayer cpe) {
				MultiPlayerGameMode interactionMngr = Minecraft.getInstance().gameMode;
				if (interactionMngr.isDestroying())
					return cpe.hasCorrectToolForDrops(entity.level.getBlockState(interactionMngr.destroyBlockPos));
			}
			return false;
		}
	}
}
