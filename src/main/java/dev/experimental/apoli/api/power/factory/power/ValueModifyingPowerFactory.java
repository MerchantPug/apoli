package dev.experimental.apoli.api.power.factory.power;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.power.IValueModifyingPower;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public abstract class ValueModifyingPowerFactory<T extends IValueModifyingPowerConfiguration> extends PowerFactory<T> implements IValueModifyingPower<T> {
	protected ValueModifyingPowerFactory(Codec<T> codec) {
		super(codec);
	}

	protected ValueModifyingPowerFactory(Codec<T> codec, boolean allowConditions) {
		super(codec, allowConditions);
	}

	@Override
	public List<EntityAttributeModifier> getModifiers(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return configuration.getConfiguration().modifiers().getContent();
	}
}
