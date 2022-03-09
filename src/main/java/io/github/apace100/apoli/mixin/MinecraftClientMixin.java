package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.SelfGlowPower;
import io.github.edwinmindcraft.apoli.common.power.EntityGlowPower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftClientMixin {

	@Shadow
	public LocalPlayer player;

	@Inject(method = "shouldEntityAppearGlowing", at = @At("RETURN"), cancellable = true)
	private void makeEntitiesGlow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
            if (this.player != null) {
				//EntityGlowPower incorporates both Self and Other glow powers.
                if (this.player != entity && EntityGlowPower.shouldGlow(this.player, entity))
                    cir.setReturnValue(true);
            }
        }
	}
}
