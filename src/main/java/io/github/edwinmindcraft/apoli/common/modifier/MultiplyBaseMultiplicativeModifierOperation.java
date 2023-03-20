package io.github.edwinmindcraft.apoli.common.modifier;

import io.github.edwinmindcraft.apoli.api.power.factory.ModifierOperation;

public class MultiplyBaseMultiplicativeModifierOperation extends ModifierOperation {
    public MultiplyBaseMultiplicativeModifierOperation() {
        super(Phase.BASE, 200, (values, base, current) -> {
            double value = current;
            for(double v : values) {
                value *= (1 + v);
            }
            return value;
        });
    }
}
