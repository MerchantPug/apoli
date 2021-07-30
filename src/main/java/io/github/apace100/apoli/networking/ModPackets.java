package io.github.apace100.apoli.networking;

import io.github.apace100.apoli.Apoli;
import net.minecraft.resources.ResourceLocation;

public class ModPackets {

    public static final ResourceLocation HANDSHAKE = Apoli.identifier("handshake");

    public static final ResourceLocation USE_ACTIVE_POWERS = Apoli.identifier("use_active_powers");
    public static final ResourceLocation POWER_LIST = Apoli.identifier("power_list");

    public static final ResourceLocation PLAYER_LANDED = Apoli.identifier("player_landed");
}
