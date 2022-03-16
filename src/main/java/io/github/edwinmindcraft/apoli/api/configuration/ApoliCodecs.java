package io.github.edwinmindcraft.apoli.api.configuration;

import com.mojang.serialization.MapCodec;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class ApoliCodecs {
	@Deprecated
	public static MapCodec<Vec3> vec3d(String xName, String yName, String zName) {
		return CalioCodecHelper.vec3d(xName, yName, zName);
	}

	@Deprecated
	public static MapCodec<Vec3> vec3d(String prefix) {
		return CalioCodecHelper.vec3d(prefix);
	}

	public static MapCodec<BlockPos> blockPos(String xName, String yName, String zName) {
		return CalioCodecHelper.blockPos(xName, yName, zName);
	}

	public static MapCodec<BlockPos> blockPos(String prefix) {
		return CalioCodecHelper.blockPos(prefix);
	}

	@Deprecated
	public static MapCodec<Vec3> VEC3D = CalioCodecHelper.VEC3D;
	public static MapCodec<BlockPos> BLOCK_POS = CalioCodecHelper.BLOCK_POS;
}
