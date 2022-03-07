package io.github.edwinmindcraft.apoli.common.network;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.registry.ApoliDynamicRegistries;
import io.github.edwinmindcraft.calio.api.CalioAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.WritableRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class S2CSynchronizePowerContainer {

	@Nullable
	public static S2CSynchronizePowerContainer forEntity(LivingEntity living) {
		return IPowerContainer.get(living).map(container -> {
			Multimap<ResourceLocation, ResourceLocation> powerSources = HashMultimap.create();
			Map<ResourceLocation, Tag> data = new HashMap<>();
			for (ResourceLocation power : container.getPowerNames()) {
				for (ResourceLocation source : container.getSources(power)) {
					powerSources.put(power, source);
				}
				ConfiguredPower<?, ?> configuredPower = container.getPower(power);
				if (configuredPower == null) {
					Apoli.LOGGER.warn("Invalid power container capability for entity {}", living.getScoreboardName());
					continue;
				}
				Tag tag = configuredPower.serialize(living, container);
				if (tag instanceof CompoundTag compound && compound.isEmpty())
					continue;
				data.put(power, tag);
			}
			return new S2CSynchronizePowerContainer(living.getId(), powerSources, data);
		}).orElse(null);
	}

	public static S2CSynchronizePowerContainer decode(FriendlyByteBuf buffer) {
		Multimap<ResourceLocation, ResourceLocation> powerSources = HashMultimap.create();
		Map<ResourceLocation, Tag> data = new HashMap<>();
		int entity = buffer.readInt();
		int powerCount = buffer.readVarInt();
		for (int i = 0; i < powerCount; i++) {
			ResourceLocation power = buffer.readResourceLocation();
			int sourceCount = buffer.readVarInt();
			for (int j = 0; j < sourceCount; j++) {
				ResourceLocation source = buffer.readResourceLocation();
				powerSources.put(power, source);
			}
			if (buffer.readBoolean()) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null)
					data.put(power, tag.get("Data"));
			}
		}
		return new S2CSynchronizePowerContainer(entity, powerSources, data);
	}

	private final int entity;
	private final Multimap<ResourceLocation, ResourceLocation> powerSources;
	private final Map<ResourceLocation, Tag> data;

	public S2CSynchronizePowerContainer(int entity, Multimap<ResourceLocation, ResourceLocation> powerSources, Map<ResourceLocation, Tag> data) {
		this.entity = entity;
		this.powerSources = powerSources;
		this.data = data;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entity);
		buffer.writeVarInt(this.powerSources.keySet().size());
		WritableRegistry<ConfiguredPower<?, ?>> configuredPowers = CalioAPI.getDynamicRegistries().get(ApoliDynamicRegistries.CONFIGURED_POWER_KEY);
		for (Map.Entry<ResourceLocation, Collection<ResourceLocation>> powerEntry : this.powerSources.asMap().entrySet()) {
			ResourceLocation power = powerEntry.getKey();
			Collection<ResourceLocation> sources = powerEntry.getValue();
			buffer.writeResourceLocation(power);
			buffer.writeVarInt(sources.size());
			sources.forEach(buffer::writeResourceLocation);
			Tag tag = this.data.get(power);
			if (tag == null || (tag instanceof CompoundTag compound && compound.isEmpty()))
				buffer.writeBoolean(false);
			else {
				buffer.writeBoolean(true);
				CompoundTag temp = new CompoundTag();
				temp.put("Data", tag);
				buffer.writeNbt(temp);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	private void handleSync() {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null)
			return;
		Entity entity = level.getEntity(this.entity);
		if (entity instanceof LivingEntity living)
			IPowerContainer.get(living).ifPresent(x -> x.handle(this.powerSources, this.data));
	}

	public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
		contextSupplier.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::handleSync));
		contextSupplier.get().setPacketHandled(true);
	}
}
