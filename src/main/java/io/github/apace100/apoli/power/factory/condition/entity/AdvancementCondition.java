package io.github.apace100.apoli.power.factory.condition.entity;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.mixin.ClientAdvancementManagerAccessor;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.EntityConditions;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;

public class AdvancementCondition {

    private static boolean condition(SerializableData.Instance data, Entity entity) {
        Identifier id = data.getId("advancement");
        if (entity.getWorld().isClient()) {
            if(entity instanceof ClientPlayerEntity) {
                ClientAdvancementManager advancementManager = MinecraftClient.getInstance().getNetworkHandler().getAdvancementHandler();
                Advancement advancement = advancementManager.getManager().get(data.getId("id"));
                if(advancement != null) {
                    Map<Advancement, AdvancementProgress> progressMap = ((ClientAdvancementManagerAccessor)advancementManager).getAdvancementProgresses();
                    if(progressMap.containsKey(advancement)) {
                        return progressMap.get(advancement).isDone();
                    }
                }
                // We don't want to print an error here if the advancement does not exist,
                // because on the client-side the advancement could just not have been received from the server.
            }
            return false;
        } else if(entity instanceof ServerPlayerEntity) {
            Advancement advancement = entity.getServer().getAdvancementLoader().get(id);
            if(advancement == null) {
                Apoli.LOGGER.warn("Advancement \"" + id + "\" did not exist, but was referenced in an \"origins:advancement\" condition.");
            } else {
                return ((ServerPlayerEntity)entity).getAdvancementTracker().getProgress(advancement).isDone();
            }
        }
        return false;
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(Apoli.identifier("advancement"), new SerializableData()
                .add("advancement", SerializableDataTypes.IDENTIFIER),
                UsingEffectiveToolCondition::condition);
    }

}
