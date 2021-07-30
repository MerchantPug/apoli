package dev.experimental.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;

public record OffsetConfiguration<T>(T value, int x, int y, int z) implements IDynamicFeatureConfiguration {
	public static <T> Codec<OffsetConfiguration<T>> codec(String name, Codec<T> codec) {
		return codec(codec.fieldOf(name));
	}

	public static <T> Codec<OffsetConfiguration<T>> codec(MapCodec<T> codec) {
		return RecordCodecBuilder.create(instance -> instance.group(
				codec.forGetter(OffsetConfiguration::value),
				Codec.INT.optionalFieldOf("x", 0).forGetter(OffsetConfiguration::x),
				Codec.INT.optionalFieldOf("y", 0).forGetter(OffsetConfiguration::y),
				Codec.INT.optionalFieldOf("z", 0).forGetter(OffsetConfiguration::z)
		).apply(instance, OffsetConfiguration::new));
	}

	public BlockPos asBlockPos() {
		return new BlockPos(this.x, this.y, this.z);
	}
}
