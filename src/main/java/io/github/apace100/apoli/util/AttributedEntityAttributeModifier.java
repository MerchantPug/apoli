package io.github.apace100.apoli.util;

public record AttributedEntityAttributeModifier(EntityAttribute attribute, EntityAttributeModifier modifier) {

    public EntityAttributeModifier getModifier() {
        return this.modifier;
    }

    public EntityAttribute getAttribute() {
        return this.attribute;
    }
}
