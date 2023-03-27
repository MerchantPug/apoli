package io.github.edwinmindcraft.apoli.common.modifier;

import io.github.edwinmindcraft.apoli.api.power.factory.ModifierOperation;

public class AddBaseLateModifierOperation extends ModifierOperation {
    public AddBaseLateModifierOperation() {
        super(Phase.BASE, 300, (values, base, current) -> {
            double value = current;
            for(double v : values) {
                value += v;
            }
            return value;
        });
    }
}
