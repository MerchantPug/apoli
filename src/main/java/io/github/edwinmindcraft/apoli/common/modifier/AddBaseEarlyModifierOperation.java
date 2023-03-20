package io.github.edwinmindcraft.apoli.common.modifier;

import io.github.edwinmindcraft.apoli.api.power.factory.ModifierOperation;

public class AddBaseEarlyModifierOperation extends ModifierOperation {
    public AddBaseEarlyModifierOperation() {
        super(Phase.BASE, 0, (values, base, current) -> base + values.stream().reduce(0.0, Double::sum));
    }
}
