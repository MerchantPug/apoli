package io.github.edwinmindcraft.apoli.common.modifier;

import io.github.edwinmindcraft.apoli.api.power.factory.ModifierOperation;

public class MinBaseModifierOperation extends ModifierOperation {
    public MinBaseModifierOperation() {
        super(Phase.BASE, 400, (values, base, current) -> {
            double value = current;
            for(double v : values) {
                value = Math.max(v, value);
            }
            return value;
        });
    }
}
