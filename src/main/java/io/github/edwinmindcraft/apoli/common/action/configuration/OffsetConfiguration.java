package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.BlockPos;

public record OffsetConfiguration<T>(T value, int x, int y, int z) implements IDynamicFeatureConfiguration {
	public static <T> Codec<OffsetConfiguration<T>> codec(String name, Codec<T> codec) {
		return codec(codec.fieldOf(name));
	}

	public static <T> Codec<OffsetConfiguration<T>> codec(MapCodec<T> codec) {
		return RecordCodecBuilder.create(instance -> instance.group(
				codec.forGetter(OffsetConfiguration::value),
				CalioCodecHelper.optionalField(Codec.INT, "x", 0).forGetter(OffsetConfiguration::x),
				CalioCodecHelper.optionalField(Codec.INT, "y", 0).forGetter(OffsetConfiguration::y),
				CalioCodecHelper.optionalField(Codec.INT, "z", 0).forGetter(OffsetConfiguration::z)
		).apply(instance, OffsetConfiguration::new));
	}

	public BlockPos asBlockPos() {
		return new BlockPos(this.x, this.y, this.z);
	}
}
