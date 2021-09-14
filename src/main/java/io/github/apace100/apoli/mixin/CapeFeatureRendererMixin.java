package io.github.apace100.apoli.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.edwinmindcraft.apoli.common.power.ElytraFlightPower;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public class CapeFeatureRendererMixin {

	@Inject(at = @At("HEAD"), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", cancellable = true)
	private void preventCapeRendering(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, AbstractClientPlayer abstractClientPlayerEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (ElytraFlightPower.shouldRenderElytra(abstractClientPlayerEntity))
			ci.cancel();
	}
}