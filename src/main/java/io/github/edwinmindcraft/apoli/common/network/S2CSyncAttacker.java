package io.github.edwinmindcraft.apoli.common.network;

import io.github.apace100.apoli.Apoli;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.OptionalInt;
import java.util.function.Supplier;

public record S2CSyncAttacker(int self, OptionalInt attacker) {
	public static S2CSyncAttacker decode(FriendlyByteBuf buf) {
		int self = buf.readInt();
		OptionalInt attacker = buf.readBoolean() ? OptionalInt.of(buf.readInt()) : OptionalInt.empty();
		return new S2CSyncAttacker(self, attacker);
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.self());
		buf.writeBoolean(this.attacker().isPresent());
		this.attacker().ifPresent(buf::writeInt);
	}

	@OnlyIn(Dist.CLIENT)
	private void handleSync() {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null)
			return;

		Entity target = level.getEntity(this.self());
		if (!(target instanceof LivingEntity living)) {
			Apoli.LOGGER.warn("Received unknown target");
			return;
		}
		if (this.attacker().isPresent()) {
			Entity attacker = level.getEntity(this.attacker().getAsInt());
			if (!(attacker instanceof LivingEntity)) {
				Apoli.LOGGER.warn("Received unknown attacker");
				return;
			}
			living.setLastHurtByMob((LivingEntity) attacker);
		} else {
			living.setLastHurtByMob(null);
		}
	}

	public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
		contextSupplier.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::handleSync));
		contextSupplier.get().setPacketHandled(true);
	}
}
