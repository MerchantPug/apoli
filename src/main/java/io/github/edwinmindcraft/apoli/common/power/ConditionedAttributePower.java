package io.github.edwinmindcraft.apoli.common.power;

import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ConditionedAttributeConfiguration;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class ConditionedAttributePower extends PowerFactory<ConditionedAttributeConfiguration> {
	public ConditionedAttributePower() {
		super(ConditionedAttributeConfiguration.CODEC);
		this.ticking(true);
	}

	private void add(ListConfiguration<AttributedEntityAttributeModifier> configuration, LivingEntity player) {
		configuration.getContent().stream().filter(x -> player.getAttributes().hasAttribute(x.attribute())).forEach(mod -> {
			AttributeInstance attributeInstance = player.getAttribute(mod.attribute());
			if (attributeInstance != null && !attributeInstance.hasModifier(mod.modifier()))
				attributeInstance.addTransientModifier(mod.modifier());
		});
	}


	private void remove(ListConfiguration<AttributedEntityAttributeModifier> configuration, LivingEntity player) {
		configuration.getContent().stream().filter(x -> player.getAttributes().hasAttribute(x.attribute())).forEach(mod -> {
			AttributeInstance attributeInstance = player.getAttribute(mod.attribute());
			if (attributeInstance != null && attributeInstance.hasModifier(mod.modifier()))
				attributeInstance.removeModifier(mod.modifier());
		});
	}

	@Override
	public void tick(ConfiguredPower<ConditionedAttributeConfiguration, ?> configuration, LivingEntity player) {
		if (configuration.isActive(player))
			this.add(configuration.getConfiguration().modifiers(), player);
		else
			this.remove(configuration.getConfiguration().modifiers(), player);
	}

	@Override
	protected void onRemoved(ConditionedAttributeConfiguration configuration, LivingEntity player) {
		this.remove(configuration.modifiers(), player);
	}

	@Override
	protected int tickInterval(ConditionedAttributeConfiguration configuration, LivingEntity player) {
		return configuration.tickRate();
	}
}
