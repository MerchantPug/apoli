package io.github.apace100.apoli.mixin;

import io.github.edwinmindcraft.apoli.common.power.ModifyBlockRenderPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyFluidRenderPower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(RenderChunkRegion.class)
public class ChunkRendererRegionMixin {

	@Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
	private void modifyBlockRender(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		Minecraft client = Minecraft.getInstance();
		if (client.level != null && client.player != null) {
			ModifyBlockRenderPower.getRenderState(client.player, client.level, pos).ifPresent(cir::setReturnValue);
		}
	}

	@Inject(method = "getFluidState", at = @At("HEAD"), cancellable = true)
	private void modifyFluidRender(BlockPos pos, CallbackInfoReturnable<FluidState> cir) {
		Minecraft client = Minecraft.getInstance();
		if (client.level != null && client.player != null) {
			ModifyFluidRenderPower.getRenderState(client.player, client.level, pos).ifPresent(cir::setReturnValue);
		}
	}
}
