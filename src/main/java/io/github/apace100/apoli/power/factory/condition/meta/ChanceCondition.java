package io.github.apace100.apoli.power.factory.condition.meta;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;

import java.util.Random;

public class ChanceCondition {

    public static <T> boolean condition(SerializableData.Instance data, T t) {
        return new Random().nextFloat() < data.getFloat("chance");
    }

    public static <T> ConditionFactory<T> getFactory() {
        return new ConditionFactory<>(
            Apoli.identifier("chance"),
            new SerializableData()
                .add("chance", SerializableDataTypes.FLOAT),
            ChanceCondition::condition
        );
    }

}
