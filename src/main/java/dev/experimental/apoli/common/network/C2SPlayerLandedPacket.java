package dev.experimental.apoli.common.network;

import dev.experimental.apoli.common.power.ActionOnLandPower;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

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
