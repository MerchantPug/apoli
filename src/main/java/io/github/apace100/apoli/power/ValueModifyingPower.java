package io.github.apace100.apoli.power;

import java.util.LinkedList;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class ValueModifyingPower extends Power {

    private final List<AttributeModifier> modifiers = new LinkedList<>();

    public ValueModifyingPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }

    public void addModifier(AttributeModifier modifier) {
        this.modifiers.add(modifier);
    }

    public List<AttributeModifier> getModifiers() {
        return modifiers;
    }
}
