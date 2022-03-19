package io.github.apace100.apoli.integration;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.EntityEvent;

import java.util.List;

public class ModifyValueEvent extends EntityEvent {
	private final PowerFactory<?> power;
	private final double baseValue;
	private final List<AttributeModifier> modifiers;

	public ModifyValueEvent(Entity entity, PowerFactory<?> power, double baseValue, List<AttributeModifier> modifiers) {
		super(entity);
		this.power = power;
		this.baseValue = baseValue;
		this.modifiers = modifiers;
	}

	public PowerFactory<?> getPower() {
		return this.power;
	}

	public double getBaseValue() {
		return this.baseValue;
	}

	public List<AttributeModifier> getModifiers() {
		return this.modifiers;
	}

	public void addModifier(AttributeModifier modifier) {
		this.modifiers.add(modifier);
	}
}
