package io.github.edwinmindcraft.apoli.common.network;

import io.github.apace100.apoli.Apoli;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2CPlayerDismount(int entity) {
	public static S2CPlayerDismount decode(FriendlyByteBuf buffer) {
		return new S2CPlayerDismount(buffer.readInt());
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entity());
	}

	@OnlyIn(Dist.CLIENT)
	private void handleSync() {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null)
			return;
		Entity entity = level.getEntity(this.entity());
		if (entity == null) {
			Apoli.LOGGER.warn("Unknown player tried to dismount");
		} else {
			if (entity.getVehicle() instanceof Player player) {
				player.removeVehicle();
			}
		}
	}

	public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
		contextSupplier.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::handleSync));
		contextSupplier.get().setPacketHandled(true);
	}
}
