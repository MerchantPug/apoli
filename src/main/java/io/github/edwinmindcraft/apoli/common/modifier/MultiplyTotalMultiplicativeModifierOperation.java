package io.github.edwinmindcraft.apoli.common.modifier;

import io.github.edwinmindcraft.apoli.api.power.factory.ModifierOperation;

public class MultiplyTotalMultiplicativeModifierOperation extends ModifierOperation {
    public MultiplyTotalMultiplicativeModifierOperation() {
        super(Phase.TOTAL, 100, (values, base, current) -> {
            double value = current;
            for(double v : values) {
                value *= (1 + v);
            }
            return value;
        });
    }
}
