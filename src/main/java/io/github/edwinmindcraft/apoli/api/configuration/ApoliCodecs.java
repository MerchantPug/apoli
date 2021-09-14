package io.github.edwinmindcraft.apoli.api.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class ApoliCodecs {
	public static MapCodec<Vec3> vec3d(String xName, String yName, String zName) {
		return RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.DOUBLE.optionalFieldOf(xName, 0.0).forGetter(Vec3::x),
				Codec.DOUBLE.optionalFieldOf(yName, 0.0).forGetter(Vec3::y),
				Codec.DOUBLE.optionalFieldOf(zName, 0.0).forGetter(Vec3::z)
		).apply(instance, Vec3::new));
	}

	public static MapCodec<Vec3> vec3d(String prefix) {
		return vec3d(prefix + "x", prefix + "y", prefix + "z");
	}

	public static MapCodec<BlockPos> blockPos(String xName, String yName, String zName) {
		return RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.INT.optionalFieldOf(xName, 0).forGetter(BlockPos::getX),
				Codec.INT.optionalFieldOf(yName, 0).forGetter(BlockPos::getY),
				Codec.INT.optionalFieldOf(zName, 0).forGetter(BlockPos::getZ)
		).apply(instance, BlockPos::new));
	}

	public static MapCodec<BlockPos> blockPos(String prefix) {
		return blockPos(prefix + "x", prefix + "y", prefix + "z");
	}

	public static MapCodec<Vec3> VEC3D = vec3d("x", "y", "z");
	public static MapCodec<BlockPos> BLOCK_POS = blockPos("x", "y", "z");
}
