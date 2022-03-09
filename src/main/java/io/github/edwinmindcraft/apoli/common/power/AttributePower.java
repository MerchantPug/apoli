package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.AttributeConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class AttributePower extends PowerFactory<AttributeConfiguration> {
	public AttributePower() {
		super(AttributeConfiguration.CODEC, false);
	}

	@Override
	protected void onAdded(AttributeConfiguration configuration, Entity entity) {
		if (!(entity instanceof LivingEntity living) || entity.level.isClientSide())
			return;
		float previousMaxHealth = living.getMaxHealth();
		float previousHealthPercent = living.getHealth() / previousMaxHealth;
		configuration.modifiers().getContent().forEach(mod -> {
			if (living.getAttributes().hasAttribute(mod.attribute())) {
				AttributeInstance instance = living.getAttribute(mod.attribute());
				assert instance != null;
				if (!instance.hasModifier(mod.modifier()))
					instance.addTransientModifier(mod.modifier());
			}
		});
		float afterMaxHealth = living.getMaxHealth();
		if (configuration.updateHealth() && afterMaxHealth != previousMaxHealth) {
			living.setHealth(afterMaxHealth * previousHealthPercent);
		}
	}

	@Override
	protected void onRemoved(AttributeConfiguration configuration, Entity entity) {
		if (!entity.level.isClientSide() && entity instanceof LivingEntity living) {
			float previousMaxHealth = living.getMaxHealth();
			float previousHealthPercent = living.getHealth() / previousMaxHealth;
			configuration.modifiers().getContent().forEach(mod -> {
				if (living.getAttributes().hasAttribute(mod.attribute())) {
					AttributeInstance instance = living.getAttribute(mod.attribute());
					assert instance != null;
					if (instance.hasModifier(mod.modifier()))
						instance.removeModifier(mod.modifier());
				}
			});
			float afterMaxHealth = living.getMaxHealth();
			if (configuration.updateHealth() && afterMaxHealth != previousMaxHealth) {
				living.setHealth(afterMaxHealth * previousHealthPercent);
			}
		}
	}
}
