package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.AttributeConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;

public class AttributePower extends PowerFactory<AttributeConfiguration> {
	public AttributePower() {
		super(AttributeConfiguration.CODEC, false);
	}

	@Override
	protected void onAdded(AttributeConfiguration configuration, LivingEntity entity) {
		if (!entity.world.isClient()) {
			float previousMaxHealth = entity.getMaxHealth();
			float previousHealthPercent = entity.getHealth() / previousMaxHealth;
			configuration.modifiers().getContent().forEach(mod -> {
				if (entity.getAttributes().hasAttribute(mod.attribute())) {
					EntityAttributeInstance instance = entity.getAttributeInstance(mod.attribute());
					assert instance != null;
					if (!instance.hasModifier(mod.modifier()))
						instance.addTemporaryModifier(mod.modifier());
				}
			});
			float afterMaxHealth = entity.getMaxHealth();
			if (configuration.updateHealth() && afterMaxHealth != previousMaxHealth) {
				entity.setHealth(afterMaxHealth * previousHealthPercent);
			}
		}
	}

	@Override
	protected void onRemoved(AttributeConfiguration configuration, LivingEntity entity) {
		if (!entity.world.isClient) {
			float previousMaxHealth = entity.getMaxHealth();
			float previousHealthPercent = entity.getHealth() / previousMaxHealth;
			configuration.modifiers().getContent().forEach(mod -> {
				if (entity.getAttributes().hasAttribute(mod.attribute())) {
					EntityAttributeInstance instance = entity.getAttributeInstance(mod.attribute());
					assert instance != null;
					if (instance.hasModifier(mod.modifier()))
						instance.removeModifier(mod.modifier());
				}
			});
			float afterMaxHealth = entity.getMaxHealth();
			if (configuration.updateHealth() && afterMaxHealth != previousMaxHealth) {
				entity.setHealth(afterMaxHealth * previousHealthPercent);
			}
		}
	}
}
