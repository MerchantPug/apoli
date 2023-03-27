package io.github.edwinmindcraft.apoli.common.modifier;

import io.github.edwinmindcraft.apoli.api.power.factory.ModifierOperation;

public class AddTotalLateModifierOperation extends ModifierOperation {
    public AddTotalLateModifierOperation() {
        super(Phase.TOTAL, 200, (values, base, current) -> {
            double value = current;
            for(double v : values) {
                value = v;
            }
            return value;
        });
    }
}
