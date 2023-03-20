package io.github.edwinmindcraft.apoli.common.modifier;

import io.github.edwinmindcraft.apoli.api.power.factory.ModifierOperation;

public class MultiplyTotalAdditiveModifierOperation extends ModifierOperation {
    public MultiplyTotalAdditiveModifierOperation() {
        super(Phase.TOTAL, 0, (values, base, current) ->
                current + (base * values.stream().reduce(0.0, Double::sum)));
    }
}
