package io.github.apace100.apoli.power.factory.condition.entity;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.mixin.ClientPlayerInteractionManagerAccessor;
import io.github.apace100.apoli.mixin.ServerPlayerInteractionManagerAccessor;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class UsingEffectiveToolCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {
        if (entity.getWorld().isClient()) {
            if(entity instanceof ClientPlayerEntity) {
                ClientPlayerInteractionManagerAccessor interactionMngr = (ClientPlayerInteractionManagerAccessor) MinecraftClient.getInstance().interactionManager;
                if(interactionMngr.getBreakingBlock()) {
                    return ((PlayerEntity)entity).canHarvest(entity.getWorld().getBlockState(interactionMngr.getCurrentBreakingPos()));
                }
            }
            return false;
        } else {
            if(entity instanceof ServerPlayerEntity) {
                ServerPlayerInteractionManagerAccessor interactionMngr = ((ServerPlayerInteractionManagerAccessor)((ServerPlayerEntity)entity).interactionManager);
                if(interactionMngr.getMining()) {
                    return ((PlayerEntity)entity).canHarvest(entity.getWorld().getBlockState(interactionMngr.getMiningPos()));
                }
            }
            return false;
        }
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(Apoli.identifier("using_effective_tool"),
                new SerializableData(),
                UsingEffectiveToolCondition::condition);
    }

}
