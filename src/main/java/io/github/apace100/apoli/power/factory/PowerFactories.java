package io.github.apace100.apoli.power.factory;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.*;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.core.Registry;
import net.minecraft.entity.*;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class PowerFactories {

    public static void register() {
        //TODO Introduce PlayerAbilityPower
        register(new PowerFactory<>(Apoli.identifier("creative_flight"),
            new SerializableData(),
            data ->
                (type, player) -> new PlayerAbilityPower(type, player, VanillaAbilities.ALLOW_FLYING))
            .allowCondition());
    }

    private static void register(PowerFactory serializer) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }
}
