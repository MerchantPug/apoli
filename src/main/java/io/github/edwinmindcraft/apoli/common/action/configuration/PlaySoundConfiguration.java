package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record PlaySoundConfiguration(SoundEvent sound, float volume,
									 float pitch) implements IDynamicFeatureConfiguration {

	public static final Codec<PlaySoundConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.SOUND_EVENT.fieldOf("sound").forGetter(PlaySoundConfiguration::sound),
			Codec.FLOAT.optionalFieldOf("volume", 1F).forGetter(PlaySoundConfiguration::volume),
			Codec.FLOAT.optionalFieldOf("pitch", 1F).forGetter(PlaySoundConfiguration::pitch)
	).apply(instance, PlaySoundConfiguration::new));

	@Override
	public @NotNull List<String> getWarnings(@NotNull ICalioDynamicRegistryManager server) {
		if (this.sound() == null)
			return ImmutableList.of("PlaySound/Missing sound");
		return IDynamicFeatureConfiguration.super.getWarnings(server);
	}

	@Override
	public boolean isConfigurationValid() {
		return this.sound() != null;
	}
}
