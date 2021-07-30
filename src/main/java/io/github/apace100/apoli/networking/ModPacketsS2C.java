package io.github.apace100.apoli.networking;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.MultiplePowerType;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModPacketsS2C {

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientLoginNetworking.registerGlobalReceiver(ModPackets.HANDSHAKE, ModPacketsS2C::handleHandshake);
        ClientPlayConnectionEvents.INIT.register(((clientPlayNetworkHandler, minecraftClient) -> {
            ClientPlayNetworking.registerReceiver(ModPackets.POWER_LIST, ModPacketsS2C::receivePowerList);
        }));
    }


    @Environment(EnvType.CLIENT)
    private static CompletableFuture<FriendlyByteBuf> handleHandshake(Minecraft minecraftClient, ClientHandshakePacketListenerImpl clientLoginNetworkHandler, FriendlyByteBuf packetByteBuf, Consumer<GenericFutureListener<? extends Future<? super Void>>> genericFutureListenerConsumer) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(Apoli.SEMVER.length);
        for(int i = 0; i < Apoli.SEMVER.length; i++) {
            buf.writeInt(Apoli.SEMVER[i]);
        }
        return CompletableFuture.completedFuture(buf);
    }

    @Environment(EnvType.CLIENT)
    private static void receivePowerList(Minecraft minecraftClient, ClientPacketListener clientPlayNetworkHandler, FriendlyByteBuf packetByteBuf, PacketSender packetSender) {
        int powerCount = packetByteBuf.readInt();
        HashMap<ResourceLocation, PowerType> factories = new HashMap<>();
        for(int i = 0; i < powerCount; i++) {
            ResourceLocation powerId = packetByteBuf.readResourceLocation();
            ResourceLocation factoryId = packetByteBuf.readResourceLocation();
            try {
                PowerFactory factory = ApoliRegistries.POWER_FACTORY.get(factoryId);
                PowerFactory.Instance factoryInstance = factory.read(packetByteBuf);
                PowerType type;
                if(packetByteBuf.readBoolean()) {
                    type = new MultiplePowerType(powerId, factoryInstance);
                    int subPowerCount = packetByteBuf.readVarInt();
                    List<ResourceLocation> subPowers = new ArrayList<>(subPowerCount);
                    for(int j = 0; j < subPowerCount; j++) {
                        subPowers.add(packetByteBuf.readResourceLocation());
                    }
                    ((MultiplePowerType)type).setSubPowers(subPowers);
                } else {
                    type = new PowerType(powerId, factoryInstance);
                }
                type.setTranslationKeys(packetByteBuf.readUtf(), packetByteBuf.readUtf());
                if (packetByteBuf.readBoolean()) {
                    type.setHidden();
                }
                factories.put(powerId, type);
            } catch(Exception e) {
                Apoli.LOGGER.error("Error while receiving \"" + powerId + "\" (factory: \"" + factoryId + "\"): " + e.getMessage());
                e.printStackTrace();
            }
        }
        minecraftClient.execute(() -> {
            PowerTypeRegistry.clear();
            factories.forEach(PowerTypeRegistry::register);
        });
    }
}
