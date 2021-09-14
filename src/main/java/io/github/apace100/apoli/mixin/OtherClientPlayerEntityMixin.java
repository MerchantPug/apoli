package io.github.apace100.apoli.mixin;

import com.mojang.authlib.GameProfile;
import io.github.edwinmindcraft.apoli.common.power.ParticlePower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RemotePlayer.class)
public abstract class OtherClientPlayerEntityMixin extends AbstractClientPlayer {

	public OtherClientPlayerEntityMixin(ClientLevel world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		if (Minecraft.getInstance().player != null && !this.isInvisibleTo(Minecraft.getInstance().player))
			ParticlePower.renderParticles(this);
	}
}
