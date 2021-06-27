package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.configuration.NoConfiguration;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import io.github.apace100.apoli.mixin.ClientPlayerInteractionManagerAccessor;
import io.github.apace100.apoli.mixin.ServerPlayerInteractionManagerAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class UsingEffectiveToolCondition extends EntityCondition<NoConfiguration> {

	public UsingEffectiveToolCondition() {
		super(NoConfiguration.CODEC);
	}

	protected boolean checkClient(LivingEntity entity) {
		return false;
	}

	@Override
	public boolean check(NoConfiguration configuration, LivingEntity entity) {
		if (entity instanceof ServerPlayerEntity spe) {
			ServerPlayerInteractionManagerAccessor interactionMngr = (ServerPlayerInteractionManagerAccessor) ((ServerPlayerEntity) entity).interactionManager;
			if (interactionMngr.getMining()) {
				return spe.canHarvest(entity.world.getBlockState(interactionMngr.getMiningPos()));
			}
		}
		return this.checkClient(entity);
	}

	@Environment(EnvType.CLIENT)
	public static class Client extends UsingEffectiveToolCondition {
		@Override
		protected boolean checkClient(LivingEntity entity) {
			if (entity instanceof ClientPlayerEntity cpe) {
				ClientPlayerInteractionManagerAccessor interactionMngr = (ClientPlayerInteractionManagerAccessor) MinecraftClient.getInstance().interactionManager;
				if (interactionMngr.getBreakingBlock())
					return cpe.canHarvest(entity.world.getBlockState(interactionMngr.getCurrentBreakingPos()));
			}
			return false;
		}
	}
}
