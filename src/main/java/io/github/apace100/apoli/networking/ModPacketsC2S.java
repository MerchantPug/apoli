package io.github.apace100.apoli.networking;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import java.util.List;
import java.util.Random;

public class ModPacketsC2S {

    public static void register() {
        if(Apoli.PERFORM_VERSION_CHECK) {
            ServerLoginConnectionEvents.QUERY_START.register(ModPacketsC2S::handshake);
            ServerLoginNetworking.registerGlobalReceiver(ModPackets.HANDSHAKE, ModPacketsC2S::handleHandshakeReply);
        }
        ServerPlayNetworking.registerGlobalReceiver(ModPackets.USE_ACTIVE_POWERS, ModPacketsC2S::useActivePowers);
        ServerPlayNetworking.registerGlobalReceiver(ModPackets.PLAYER_LANDED, ModPacketsC2S::playerLanded);
    }

    private static void playerLanded(MinecraftServer minecraftServer, ServerPlayer playerEntity, ServerGamePacketListenerImpl serverPlayNetworkHandler, FriendlyByteBuf packetByteBuf, PacketSender packetSender) {
        minecraftServer.execute(() -> PowerHolderComponent.getPowers(playerEntity, ActionOnLandPower.class).forEach(ActionOnLandPower::executeAction));
    }

    private static void useActivePowers(MinecraftServer minecraftServer, ServerPlayer playerEntity, ServerGamePacketListenerImpl serverPlayNetworkHandler, FriendlyByteBuf packetByteBuf, PacketSender packetSender) {
        int count = packetByteBuf.readInt();
        ResourceLocation[] powerIds = new ResourceLocation[count];
        for(int i = 0; i < count; i++) {
            powerIds[i] = packetByteBuf.readResourceLocation();
        }
        minecraftServer.execute(() -> {
            PowerHolderComponent component = PowerHolderComponent.KEY.get(playerEntity);
            for(ResourceLocation id : powerIds) {
                PowerType<?> type = PowerTypeRegistry.get(id);
                Power power = component.getPower(type);
                if(power instanceof Active) {
                    ((Active)power).onUse();
                }
            }
        });
    }

    private static void handleHandshakeReply(MinecraftServer minecraftServer, ServerLoginPacketListenerImpl serverLoginNetworkHandler, boolean understood, FriendlyByteBuf packetByteBuf, ServerLoginNetworking.LoginSynchronizer loginSynchronizer, PacketSender packetSender) {
        if (understood) {
            int clientSemVerLength = packetByteBuf.readInt();
            int[] clientSemVer = new int[clientSemVerLength];
            boolean mismatch = clientSemVerLength != Apoli.SEMVER.length;
            for(int i = 0; i < clientSemVerLength; i++) {
                clientSemVer[i] = packetByteBuf.readInt();
                if(i < clientSemVerLength - 1 && clientSemVer[i] != Apoli.SEMVER[i]) {
                    mismatch = true;
                }
            }
            if(mismatch) {
                StringBuilder clientVersionString = new StringBuilder();
                for(int i = 0; i < clientSemVerLength; i++) {
                    clientVersionString.append(clientSemVer[i]);
                    if(i < clientSemVerLength - 1) {
                        clientVersionString.append(".");
                    }
                }
                serverLoginNetworkHandler.disconnect(new TranslatableComponent("apoli.gui.version_mismatch", Apoli.VERSION, clientVersionString));
            }
        } else {
            serverLoginNetworkHandler.disconnect(new TextComponent("This server requires you to install the Apoli mod (v" + Apoli.VERSION + ") to play."));
        }
    }

    private static void handshake(ServerLoginPacketListenerImpl serverLoginNetworkHandler, MinecraftServer minecraftServer, PacketSender packetSender, ServerLoginNetworking.LoginSynchronizer loginSynchronizer) {
        packetSender.sendPacket(ModPackets.HANDSHAKE, PacketByteBufs.empty());
    }
}
