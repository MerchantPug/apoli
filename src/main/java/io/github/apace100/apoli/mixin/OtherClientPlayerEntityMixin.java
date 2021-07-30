package io.github.apace100.apoli.mixin;

import com.mojang.authlib.GameProfile;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ParticlePower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;

@Mixin(RemotePlayer.class)
public abstract class OtherClientPlayerEntityMixin extends AbstractClientPlayer {

    public OtherClientPlayerEntityMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo info) {
        if(!this.isInvisibleTo(Minecraft.getInstance().player)) {
            PowerHolderComponent component = PowerHolderComponent.KEY.get(this);
            List<ParticlePower> particlePowers = component.getPowers(ParticlePower.class);
            for (ParticlePower particlePower : particlePowers) {
                if(this.tickCount % particlePower.getFrequency() == 0) {
                    level.addParticle(particlePower.getParticle(), this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0, 0, 0);
                }
            }
        }
    }
}
