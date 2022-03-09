package io.github.edwinmindcraft.apoli.common.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.INightVisionPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.entity.Entity;

public class NightVisionPower extends PowerFactory<FieldConfiguration<Float>> implements INightVisionPower<FieldConfiguration<Float>> {
	public NightVisionPower() {
		super(FieldConfiguration.codec(Codec.FLOAT, "strength", 1.0F));
	}

	@Override
	public float getStrength(ConfiguredPower<FieldConfiguration<Float>, ?> configuration, Entity player) {
		return configuration.getConfiguration().value();
	}
}
