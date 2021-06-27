package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.configuration.ListConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.ConditionedAttributeConfiguration;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;

public class ConditionedAttributePower extends PowerFactory<ConditionedAttributeConfiguration> {
	public ConditionedAttributePower() {
		super(ConditionedAttributeConfiguration.CODEC);
		this.ticking(true);
	}

	private void add(ListConfiguration<AttributedEntityAttributeModifier> configuration, LivingEntity player) {
		configuration.getContent().stream().filter(x -> player.getAttributes().hasAttribute(x.attribute())).forEach(mod -> {
			EntityAttributeInstance attributeInstance = player.getAttributeInstance(mod.attribute());
			if (!attributeInstance.hasModifier(mod.modifier())) attributeInstance.addTemporaryModifier(mod.modifier());
		});
	}


	private void remove(ListConfiguration<AttributedEntityAttributeModifier> configuration, LivingEntity player) {
		configuration.getContent().stream().filter(x -> player.getAttributes().hasAttribute(x.attribute())).forEach(mod -> {
			EntityAttributeInstance attributeInstance = player.getAttributeInstance(mod.attribute());
			if (attributeInstance.hasModifier(mod.modifier())) attributeInstance.removeModifier(mod.modifier());
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
