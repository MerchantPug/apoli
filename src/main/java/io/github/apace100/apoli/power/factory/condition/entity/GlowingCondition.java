package io.github.apace100.apoli.power.factory.condition.entity;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.EntityConditions;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.checkerframework.checker.units.qual.C;

public class GlowingCondition {

    private static boolean condition(SerializableData.Instance data, Entity entity) {
        if (entity.getWorld().isClient()) {
            return MinecraftClient.getInstance().hasOutline(entity);
        } else {
            return entity.isGlowing();
        }
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(Apoli.identifier("glowing"),
                new SerializableData(),
                GlowingCondition::condition);
    }
}
