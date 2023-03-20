package io.github.edwinmindcraft.apoli.common.modifier;

import io.github.edwinmindcraft.apoli.api.power.factory.ModifierOperation;

public class MaxBaseModifierOperation extends ModifierOperation {
    public MaxBaseModifierOperation() {
        super(Phase.BASE, 400, (values, base, current) -> {
            double value = current;
            for(double v : values) {
                value = Math.min(v, value);
            }
            return value;
        });
    }
}
