package io.github.edwinmindcraft.apoli.common.network;

import io.github.apace100.apoli.Apoli;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2CPlayerMount(int entity, int vehicle) {
	public static S2CPlayerMount decode(FriendlyByteBuf buffer) {
		return new S2CPlayerMount(buffer.readInt(), buffer.readInt());
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entity());
		buffer.writeInt(this.vehicle());
	}

	@OnlyIn(Dist.CLIENT)
	private void handleSync() {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null)
			return;
		Entity entity = level.getEntity(this.entity());
		Entity vehicle = level.getEntity(this.vehicle());
		if (entity == null) {
			Apoli.LOGGER.warn("Received passenger for unknown player");
		} else if (vehicle == null) {
			Apoli.LOGGER.warn("Received unknown passenger for player");
		} else {
			if (entity.startRiding(vehicle, true))
				Apoli.LOGGER.info("{} started riding {}", entity.getDisplayName().getString(), vehicle.getDisplayName().getString());
			else
				Apoli.LOGGER.warn("{} failed to start riding {}", entity.getDisplayName().getString(), vehicle.getDisplayName().getString());
		}
	}

	public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
		contextSupplier.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::handleSync));
		contextSupplier.get().setPacketHandled(true);
	}
}
