package io.github.apace100.apoli.util;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public record AttributedEntityAttributeModifier(EntityAttribute attribute, EntityAttributeModifier modifier) {

    public EntityAttributeModifier getModifier() {
        return this.modifier;
    }

    public EntityAttribute getAttribute() {
        return this.attribute;
    }
}
