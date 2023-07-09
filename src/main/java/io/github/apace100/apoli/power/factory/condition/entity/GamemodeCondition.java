package io.github.apace100.apoli.power.factory.condition.entity;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.mixin.ClientPlayerInteractionManagerAccessor;
import io.github.apace100.apoli.mixin.ServerPlayerInteractionManagerAccessor;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.EntityConditions;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

public class GamemodeCondition {

    private static boolean condition(SerializableData.Instance data, Entity entity) {
        if (entity.getWorld().isClient()) {
            if(entity instanceof ClientPlayerEntity) {
                ClientPlayerInteractionManagerAccessor interactionMngr = (ClientPlayerInteractionManagerAccessor) MinecraftClient.getInstance().interactionManager;
                return interactionMngr.getGameMode().getName().equals(data.getString("gamemode"));
            }
        } else if(entity instanceof ServerPlayerEntity) {
            ServerPlayerInteractionManagerAccessor interactionMngr = ((ServerPlayerInteractionManagerAccessor)((ServerPlayerEntity)entity).interactionManager);
            return interactionMngr.getGameMode().getName().equals(data.getString("gamemode"));
        }
        return false;
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(Apoli.identifier("gamemode"),
                new SerializableData(),
                GamemodeCondition::condition);
    }

}
