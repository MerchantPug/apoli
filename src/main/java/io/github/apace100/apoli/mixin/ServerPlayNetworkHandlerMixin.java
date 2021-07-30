package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.access.EndRespawningEntity;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow
    public ServerPlayer player;

    @Inject(method = "onClientStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;respawnPlayer(Lnet/minecraft/server/network/ServerPlayerEntity;Z)Lnet/minecraft/server/network/ServerPlayerEntity;", ordinal = 0))
    private void saveEndRespawnStatus(ServerboundClientCommandPacket packet, CallbackInfo ci) {
        ((EndRespawningEntity)this.player).setEndRespawning(true);
    }

    @Inject(method = "onClientStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/ChangedDimensionCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/registry/RegistryKey;)V"))
    private void undoEndRespawnStatus(ServerboundClientCommandPacket packet, CallbackInfo ci) {
        ((EndRespawningEntity)this.player).setEndRespawning(false);
    }
}
