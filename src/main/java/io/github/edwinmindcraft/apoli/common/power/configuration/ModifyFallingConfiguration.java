package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;

public record ModifyFallingConfiguration(double velocity, boolean takeFallDamage) implements IDynamicFeatureConfiguration {
	public static final Codec<ModifyFallingConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.DOUBLE.fieldOf("velocity").forGetter(ModifyFallingConfiguration::velocity),
			Codec.BOOL.optionalFieldOf("take_fall_damage", true).forGetter(ModifyFallingConfiguration::takeFallDamage)
	).apply(instance, ModifyFallingConfiguration::new));
}
