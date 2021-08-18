package io.github.apace100.apoli.util;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributedEntityAttributeModifier(Attribute attribute, AttributeModifier modifier) {

	public AttributeModifier getModifier() {
		return this.modifier;
	}

	public Attribute getAttribute() {
		return this.attribute;
	}
}
