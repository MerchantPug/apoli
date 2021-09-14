package io.github.apace100.apoli.mixin;

import io.github.edwinmindcraft.apoli.common.power.ElytraFlightPower;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ElytraLayer.class)
public class ElytraFeatureRendererMixin {

	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
	private void modifyEquippedStackToElytra(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (ElytraFlightPower.shouldRenderElytra(entity) && !entity.isInvisible())
			cir.setReturnValue(true);
	}
}
