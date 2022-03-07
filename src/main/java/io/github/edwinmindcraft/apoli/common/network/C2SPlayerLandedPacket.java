package io.github.edwinmindcraft.apoli.common.network;

import io.github.edwinmindcraft.apoli.common.power.ActionOnLandPower;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Deprecated
public class C2SPlayerLandedPacket {
	public static C2SPlayerLandedPacket decode(FriendlyByteBuf buffer) {
		return new C2SPlayerLandedPacket();
	}

	public void encode(FriendlyByteBuf buffer) {

	}

	public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
		contextSupplier.get().enqueueWork(() -> ActionOnLandPower.execute(contextSupplier.get().getSender()));
		contextSupplier.get().setPacketHandled(true);
	}
}
