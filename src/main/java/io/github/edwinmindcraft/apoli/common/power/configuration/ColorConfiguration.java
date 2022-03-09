package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredFactory;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public record ColorConfiguration(float red, float green, float blue,
								 float alpha) implements IDynamicFeatureConfiguration {
	public static final Codec<ColorConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(Codec.FLOAT, "red", 1.0F).forGetter(ColorConfiguration::red),
			CalioCodecHelper.optionalField(Codec.FLOAT, "green", 1.0F).forGetter(ColorConfiguration::green),
			CalioCodecHelper.optionalField(Codec.FLOAT, "blue", 1.0F).forGetter(ColorConfiguration::blue),
			CalioCodecHelper.optionalField(Codec.FLOAT, "alpha", 1.0F).forGetter(ColorConfiguration::alpha)
	).apply(instance, ColorConfiguration::new));

	public static final MapCodec<ColorConfiguration> NO_ALPHA = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CalioCodecHelper.optionalField(Codec.FLOAT, "red", 1.0F).forGetter(ColorConfiguration::red),
			CalioCodecHelper.optionalField(Codec.FLOAT, "green", 1.0F).forGetter(ColorConfiguration::green),
			CalioCodecHelper.optionalField(Codec.FLOAT, "blue", 1.0F).forGetter(ColorConfiguration::blue)
	).apply(instance, ColorConfiguration::new));

	public static Optional<ColorConfiguration> forPower(Entity entity, PowerFactory<ColorConfiguration> factory) {
		return IPowerContainer.getPowers(entity, factory).stream().map(ConfiguredFactory::getConfiguration).reduce(ColorConfiguration::merge);
	}

	public ColorConfiguration(float red, float green, float blue) {
		this(red, green, blue, 1.0F);
	}

	public ColorConfiguration merge(ColorConfiguration other) {
		return new ColorConfiguration(this.red() * other.red(), this.green() * other.green(), this.blue() * other.blue(), Math.min(this.alpha(), other.alpha()));
	}
}
