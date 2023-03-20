package io.github.edwinmindcraft.apoli.common.modifier;

import io.github.edwinmindcraft.apoli.api.power.factory.ModifierOperation;

public class MultiplyBaseAdditiveModifierOperation extends ModifierOperation {
    public MultiplyBaseAdditiveModifierOperation() {
        super(Phase.BASE, 100, (values, base, current) ->
                current + (base * values.stream().reduce(0.0, Double::sum)));
    }
}
