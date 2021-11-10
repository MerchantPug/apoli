package io.github.apace100.apoli.power.factory.action;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredFactory;
import io.github.edwinmindcraft.apoli.api.power.IFactory;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockAction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class ActionFactory<T> {

	private final ResourceLocation identifier;
	protected SerializableData data;
	private final BiConsumer<SerializableData.Instance, T> effect;
	private Codec<?> codec;
	private IFactory<?, ?, ?> factory;

	public ActionFactory(ResourceLocation identifier, SerializableData data, BiConsumer<SerializableData.Instance, T> effect) {
		this.identifier = identifier;
		this.effect = effect;
		this.data = data;
		this.data.add("inverted", SerializableDataTypes.BOOLEAN, false);
	}
	public ActionFactory(ResourceLocation identifier, Codec<?> codec, IFactory<?, ?, ?> factory) {
		this.identifier = identifier;
		this.codec = codec;
		this.factory = factory;
		//FIXME This needs to change.
		this.effect = null;
	}

	public <C extends IFactory<?, ?, ?>, V extends C> C getWrapped(Codec<C> codec, BiFunction<SerializableData, BiConsumer<SerializableData.Instance, T>, V> constructor) {
		if (this.codec == null || this.factory == null) {
			this.codec = codec;
			this.factory = constructor.apply(this.data, this.effect);
		}
		return (C) this.factory;
	}

	public class Instance implements Consumer<T> {

		private final SerializableData.Instance dataInstance;

		private Instance(SerializableData.Instance data) {
			this.dataInstance = data;
		}

		public void write(FriendlyByteBuf buf) {
			buf.writeResourceLocation(ActionFactory.this.identifier);
			ActionFactory.this.data.write(buf, this.dataInstance);
		}

		@Override
		public void accept(T t) {
			ActionFactory.this.effect.accept(this.dataInstance, t);
		}
	}

	public ResourceLocation getSerializerId() {
		return this.identifier;
	}

	public Instance read(JsonObject json) {
		return new Instance(this.data.read(json));
	}

	public Instance read(FriendlyByteBuf buffer) {
		return new Instance(this.data.read(buffer));
	}
}
