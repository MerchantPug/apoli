package io.github.apace100.apoli.condition.type.bientity;

import net.minecraft.entity.Entity;

import java.util.Objects;

public class RidingConditionType {

    public static boolean condition(Entity actor, Entity target) {
        return actor != null
            && target != null
            && Objects.equals(actor.getVehicle(), target);
    }

}
