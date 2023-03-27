package io.github.edwinmindcraft.apoli.common.modifier;

import io.github.edwinmindcraft.apoli.api.power.factory.ModifierOperation;

public class SetTotalModifierOperation extends ModifierOperation {
    public SetTotalModifierOperation() {
        super(Phase.TOTAL, 500, (values, base, current) -> {
            double value = current;
            for(double v : values) {
                value = v;
            }
            return value;
        });
    }
}
