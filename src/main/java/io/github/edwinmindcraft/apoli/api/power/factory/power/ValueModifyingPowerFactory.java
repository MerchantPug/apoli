package io.github.edwinmindcraft.apoli.api.power.factory.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.power.IValueModifyingPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import java.util.List;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public abstract class ValueModifyingPowerFactory<T extends IValueModifyingPowerConfiguration> extends PowerFactory<T> implements IValueModifyingPower<T> {
	protected ValueModifyingPowerFactory(Codec<T> codec) {
		super(codec);
	}

	protected ValueModifyingPowerFactory(Codec<T> codec, boolean allowConditions) {
		super(codec, allowConditions);
	}

	@Override
	public List<AttributeModifier> getModifiers(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return configuration.getConfiguration().modifiers().getContent();
	}
}
