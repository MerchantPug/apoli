package io.github.edwinmindcraft.apoli.common.modifier;

import io.github.edwinmindcraft.apoli.api.power.factory.ModifierOperation;

public class MinTotalModifierOperation extends ModifierOperation {
    public MinTotalModifierOperation() {
        super(Phase.TOTAL, 300, (values, base, current) -> {
            double value = current;
            for(double v : values) {
                value = Math.max(v, value);
            }
            return value;
        });
    }
}
