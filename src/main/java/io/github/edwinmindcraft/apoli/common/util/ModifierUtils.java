package io.github.edwinmindcraft.apoli.common.util;

import com.google.common.collect.ImmutableList;
import io.github.edwinmindcraft.apoli.api.power.ModifierData;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.factory.ModifierOperation;
import io.github.edwinmindcraft.apoli.common.registry.ApoliModifierOperations;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.*;
import java.util.function.Supplier;

public class ModifierUtils {
    public static ConfiguredModifier<?> createSimpleModifier(Supplier<ModifierOperation> operation, double value) {
        return new ConfiguredModifier<>(operation, new ModifierData(value, Optional.empty(), ImmutableList.of()));
    }

    public static ConfiguredModifier<?> fromAttributeModifier(AttributeModifier attributeModifier) {
        Supplier<ModifierOperation> operation = null;
        switch(attributeModifier.getOperation()) {
            case ADDITION -> operation = ApoliModifierOperations.ADD_BASE_EARLY::get;
            case MULTIPLY_BASE -> operation = ApoliModifierOperations.MULTIPLY_BASE_ADDITIVE::get;
            case MULTIPLY_TOTAL -> operation = ApoliModifierOperations.MULTIPLY_TOTAL_MULTIPLICATIVE::get;
        }
        if(operation == null) {
            throw new RuntimeException(
                    "Could not construct generic modifier from attribute modifier. Unknown operation: "
                            + attributeModifier.getOperation());
        }
        return createSimpleModifier(operation, attributeModifier.getAmount());
    }

    public static Map<ModifierOperation, List<ConfiguredModifier<?>>> sortModifiers(List<ConfiguredModifier<?>> modifiers) {
        Map<ModifierOperation, List<ConfiguredModifier<?>>> buckets = new HashMap<>();
        for(ConfiguredModifier<?> modifier : modifiers) {
            List<ConfiguredModifier<?>> list = buckets.computeIfAbsent(modifier.getFactory(), op -> new LinkedList<>());
            list.add(modifier);
        }
        return buckets;
    }

    public static double applyModifiers(Entity entity, List<ConfiguredModifier<?>> modifiers, double baseValue) {
        return applyModifiers(entity, sortModifiers(modifiers), baseValue);
    }

    public static double applyModifiers(Entity entity, Map<ModifierOperation, List<ConfiguredModifier<?>>> modifiers, double baseValue) {
        double currentBase = baseValue;
        double currentValue = baseValue;
        List<ModifierOperation> operations = new LinkedList<>(modifiers.keySet());
        operations.sort(((o1, o2) -> {
            if(o1 == o2) {
                return 0;
            } else if(o1.getPhase() == o2.getPhase()) {
                return o1.getOrder() - o2.getOrder();
            } else {
                return o1.getPhase().ordinal() - o2.getPhase().ordinal();
            }
        }));
        ModifierOperation.Phase lastPhase = ModifierOperation.Phase.BASE;
        for(ModifierOperation op : operations) {
            List<ConfiguredModifier<?>> data = modifiers.get(op);
            if(op.getPhase() != lastPhase) {
                currentBase = currentValue;
            }
            currentValue = op.apply(data, entity, currentBase, currentValue);
        }
        return currentValue;
    }
}
