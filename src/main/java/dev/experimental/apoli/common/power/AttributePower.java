package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.configuration.ListConfiguration;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;

public class AttributePower extends PowerFactory<ListConfiguration<AttributedEntityAttributeModifier>> {
	public AttributePower() {
		super(ListConfiguration.codec(ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIER, "modifier", "modifiers"), false);
	}

	@Override
	protected void onAdded(ListConfiguration<AttributedEntityAttributeModifier> configuration, LivingEntity player) {
		configuration.getContent().stream().filter(x -> player.getAttributes().hasAttribute(x.attribute())).forEach(mod -> {
			EntityAttributeInstance attributeInstance = player.getAttributeInstance(mod.attribute());
			if (!attributeInstance.hasModifier(mod.modifier())) attributeInstance.addTemporaryModifier(mod.modifier());
		});
	}

	@Override
	protected void onRemoved(ListConfiguration<AttributedEntityAttributeModifier> configuration, LivingEntity player) {
		configuration.getContent().stream().filter(x -> player.getAttributes().hasAttribute(x.attribute())).forEach(mod -> {
			EntityAttributeInstance attributeInstance = player.getAttributeInstance(mod.attribute());
			if (attributeInstance.hasModifier(mod.modifier())) attributeInstance.removeModifier(mod.modifier());
		});
	}
}
