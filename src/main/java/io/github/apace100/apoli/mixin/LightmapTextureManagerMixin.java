package io.github.apace100.apoli.mixin;

import dev.experimental.apoli.api.power.INightVisionPower;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightTexture.class)
@OnlyIn(Dist.CLIENT)
public abstract class LightmapTextureManagerMixin implements AutoCloseable {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z"), method = "updateLightTexture")
	public boolean hasStatusEffectProxy(LocalPlayer player, MobEffect effect) {
		if (effect == MobEffects.NIGHT_VISION && !player.hasEffect(MobEffects.NIGHT_VISION))
			return INightVisionPower.getNightVisionStrength(player).map(x -> true).orElseGet(() -> player.hasEffect(effect));
		return player.hasEffect(effect);
	}
}
