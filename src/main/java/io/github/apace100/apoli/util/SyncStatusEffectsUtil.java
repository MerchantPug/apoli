package io.github.apace100.apoli.util;

public class SyncStatusEffectsUtil {

   /* public static void sendStatusEffectUpdatePacket(LivingEntity living, UpdateType type, StatusEffectInstance instance) {
        if (living.world.isClient()) return;
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(living.getId());
        buf.writeByte(type.ordinal());
        if(type != UpdateType.CLEAR) {
            SerializationHelper.writeStatusEffect(buf, instance);
        }
        for (ServerPlayerEntity player : PlayerLookup.tracking(living)) {
            ServerPlayNetworking.send(player, ModPackets.SYNC_STATUS_EFFECT, buf);
        }
    }*/

	public enum UpdateType {
		CLEAR, APPLY, UPGRADE, REMOVE
	}
}