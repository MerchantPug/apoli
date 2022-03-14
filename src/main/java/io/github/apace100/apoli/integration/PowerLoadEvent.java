package io.github.apace100.apoli.integration;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.calio.api.event.DynamicRegistrationEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	public static class Post extends PowerLoadEvent {

		private final ConfiguredPower<?, ?> power;

		public Post(ResourceLocation id, JsonElement original, ConfiguredPower<?, ?> power) {
			super(id, original);
			this.power = power;
		}

		public ConfiguredPower<?, ?> getPower() {
			return this.power;
		}

		@NotNull
		public JsonElement getAdditionalData(String key) {
			return this.getOriginal().isJsonObject() && this.getOriginal().getAsJsonObject().has(key) ? this.getOriginal().getAsJsonObject().get(key) : JsonNull.INSTANCE;
		}
	}
}
