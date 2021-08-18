package io.github.apace100.apoli.mixin;

import com.mojang.authlib.GameProfile;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.common.registry.ModPowers;
import io.github.apace100.apoli.access.WaterMovingEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayer implements WaterMovingEntity {

	private boolean isMoving = false;

	public ClientPlayerEntityMixin(ClientLevel world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(at = @At("HEAD"), method = "isUnderWater", cancellable = true)
	private void allowSwimming(CallbackInfoReturnable<Boolean> cir) {
		if (IPowerContainer.hasPower(this, ModPowers.SWIMMING.get())) {
			cir.setReturnValue(true);
		} else if (IPowerContainer.hasPower(this, ModPowers.IGNORE_WATER.get())) {
			cir.setReturnValue(false);
		}
	}

	@Inject(at = @At("HEAD"), method = "aiStep")
	private void beginMovementPhase(CallbackInfo ci) {
		this.isMoving = true;
	}

	@Inject(at = @At("TAIL"), method = "aiStep")
	private void endMovementPhase(CallbackInfo ci) {
		this.isMoving = false;
	}

	public boolean isInMovementPhase() {
		return this.isMoving;
	}
}
