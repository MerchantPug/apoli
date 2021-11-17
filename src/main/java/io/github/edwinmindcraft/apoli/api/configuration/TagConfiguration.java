package io.github.edwinmindcraft.apoli.api.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import net.minecraft.tags.SerializationTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagContainer;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import java.util.Objects;

public record TagConfiguration<V>(Tag<V> value) implements IDynamicFeatureConfiguration {

	public static <T> Codec<TagConfiguration<T>> codec(Codec<Tag<T>> codec, String fieldName) {
		return codec.fieldOf(fieldName).xmap(TagConfiguration::new, TagConfiguration::value).codec();
	}


	public boolean isLoaded() {
		return SerializationTags.getInstance() != null && SerializationTags.getInstance() != TagContainer.EMPTY;
	}

	public boolean contains(V value) {
		if (!this.isLoaded())
			return false;
		return this.value().contains(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (TagConfiguration<?>) obj;
		return Objects.equals(this.value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.value);
	}

	@Override
	public String toString() {
		return "TagConfiguration[" +
			   "value=" + this.value + ']';
	}
}
