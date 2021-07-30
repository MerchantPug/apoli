package dev.experimental.apoli.common.power;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.INightVisionPower;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.entity.LivingEntity;

public class NightVisionPower extends PowerFactory<FieldConfiguration<Float>> implements INightVisionPower<FieldConfiguration<Float>> {
	public NightVisionPower() {
		super(FieldConfiguration.codec(Codec.FLOAT, "strength", 1.0F));
	}

	@Override
	public float getStrength(ConfiguredPower<FieldConfiguration<Float>, ?> configuration, LivingEntity player) {
		return configuration.getConfiguration().value();
	}
}
