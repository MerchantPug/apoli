package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.AttributeConfiguration;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class AttributePower extends PowerFactory<AttributeConfiguration> {
	public AttributePower() {
		super(AttributeConfiguration.CODEC, false);
	}

	@Override
	protected void onAdded(AttributeConfiguration configuration, LivingEntity entity) {
		if (!entity.level.isClientSide()) {
			float previousMaxHealth = entity.getMaxHealth();
			float previousHealthPercent = entity.getHealth() / previousMaxHealth;
			configuration.modifiers().getContent().forEach(mod -> {
				if (entity.getAttributes().hasAttribute(mod.attribute())) {
					AttributeInstance instance = entity.getAttribute(mod.attribute());
					assert instance != null;
					if (!instance.hasModifier(mod.modifier()))
						instance.addTransientModifier(mod.modifier());
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
		if (!entity.level.isClientSide) {
			float previousMaxHealth = entity.getMaxHealth();
			float previousHealthPercent = entity.getHealth() / previousMaxHealth;
			configuration.modifiers().getContent().forEach(mod -> {
				if (entity.getAttributes().hasAttribute(mod.attribute())) {
					AttributeInstance instance = entity.getAttribute(mod.attribute());
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
