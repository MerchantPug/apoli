package io.github.edwinmindcraft.apoli.api.event;

import io.github.edwinmindcraft.apoli.api.power.PowerData;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * This event is fired when a power is loaded.<br/>
 * The power passed to this event is the one with the
 * highest {@link PowerData#loadingPriority()} if more than
 * one power definition is found.<br/>
 * This event is {@link Cancelable}.<br/>
 * This event does not have a result.<br/>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
 */
@Cancelable
public class PowerLoadingEvent extends Event {
	private final ResourceLocation identifier;
	private final ConfiguredPower<?, ?> original;
	private final PowerData.Builder builder;

	public PowerLoadingEvent(ResourceLocation identifier, ConfiguredPower<?, ?> original, PowerData data) {
		this.identifier = identifier;
		this.original = original;
		this.builder = data.copyOf();
	}

	public ConfiguredPower<?, ?> getOriginal() {
		return this.original;
	}

	public ResourceLocation getIdentifier() {
		return this.identifier;
	}

	public PowerData.Builder getBuilder() {
		return this.builder;
	}
}
