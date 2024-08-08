package io.github.apace100.apoli.power.factory.condition.bientity;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.condition.BiEntityConditions;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.type.EntitySetPowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

public class InEntitySetCondition {

    public static boolean condition(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {

        PowerHolderComponent component = PowerHolderComponent.KEY.maybeGet(actorAndTarget.getLeft()).orElse(null);
        Power power = data.get("set");

        if (component == null || power == null || !(component.getPowerType(power) instanceof EntitySetPowerType entitySetPower)) {
            return false;
        }

        return entitySetPower.contains(actorAndTarget.getRight());

    }

    public static ConditionFactory<Pair<Entity, Entity>> getFactory() {

        ConditionFactory<Pair<Entity, Entity>> factory = new ConditionFactory<>(
            Apoli.identifier("in_entity_set"),
            new SerializableData()
                .add("set", ApoliDataTypes.POWER_REFERENCE),
            InEntitySetCondition::condition
        );

        BiEntityConditions.ALIASES.addPathAlias("in_set", factory.getSerializerId().getPath());
        return factory;

    }

}
