package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.power.AttributeModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.AttributeModifyingPowerConfiguration;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ModifyAttributePower extends AttributeModifyingPowerFactory<AttributeModifyingPowerConfiguration> {

	private final Supplier<Attribute> attribute;

	public ModifyAttributePower(Supplier<Attribute> attribute) {
		super(AttributeModifyingPowerConfiguration.CODEC);
		this.attribute = attribute;
	}

	@Override
	public @Nullable Attribute getAttribute() {
		return this.attribute.get();
	}
}
