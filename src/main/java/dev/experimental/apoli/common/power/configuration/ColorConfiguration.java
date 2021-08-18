package dev.experimental.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.ConfiguredFactory;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public record ColorConfiguration(float red, float green, float blue,
								 float alpha) implements IDynamicFeatureConfiguration {
	public static final Codec<ColorConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.optionalFieldOf("red", 1.0F).forGetter(ColorConfiguration::red),
			Codec.FLOAT.optionalFieldOf("green", 1.0F).forGetter(ColorConfiguration::green),
			Codec.FLOAT.optionalFieldOf("blue", 1.0F).forGetter(ColorConfiguration::blue),
			Codec.FLOAT.optionalFieldOf("alpha", 1.0F).forGetter(ColorConfiguration::alpha)
	).apply(instance, ColorConfiguration::new));

	public static Optional<ColorConfiguration> forPower(Entity entity, PowerFactory<ColorConfiguration> factory) {
		return IPowerContainer.getPowers(entity, factory).stream().map(ConfiguredFactory::getConfiguration).reduce(ColorConfiguration::merge);
	}

	public ColorConfiguration merge(ColorConfiguration other) {
		return new ColorConfiguration(this.red() * other.red(), this.green() * other.green(), this.blue() * other.blue(), Math.min(this.alpha(), other.alpha()));
	}
}
