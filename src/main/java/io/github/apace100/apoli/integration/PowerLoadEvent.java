package io.github.apace100.apoli.integration;

import com.google.gson.JsonElement;
import io.github.edwinmindcraft.calio.api.event.DynamicRegistrationEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * For Post load behaviour, use {@link DynamicRegistrationEvent}
 */
public class PowerLoadEvent extends Event {
	private final ResourceLocation id;
	private final JsonElement original;

	public PowerLoadEvent(ResourceLocation id, JsonElement original) {
		this.id = id;
		this.original = original.deepCopy();
	}

	public JsonElement getOriginal() {
		return this.original;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	@Cancelable
	public static class Pre extends PowerLoadEvent {

		private JsonElement json;

		public Pre(ResourceLocation id, JsonElement original) {
			super(id, original);
			this.json = original;
		}

		public JsonElement getJson() {
			return this.json;
		}

		public void setJson(JsonElement json) {
			this.json = json;
		}
	}
}
