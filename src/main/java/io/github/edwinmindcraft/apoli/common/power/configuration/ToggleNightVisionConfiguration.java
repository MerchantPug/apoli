package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.ITogglePowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

public record ToggleNightVisionConfiguration(boolean defaultState, IActivePower.Key key,
											 float strength) implements ITogglePowerConfiguration {
	public static final Codec<ToggleNightVisionConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(Codec.BOOL, "active_by_default", false).forGetter(ITogglePowerConfiguration::defaultState),
			CalioCodecHelper.optionalField(IActivePower.Key.BACKWARD_COMPATIBLE_CODEC, "key", IActivePower.Key.PRIMARY).forGetter(ITogglePowerConfiguration::key),
			CalioCodecHelper.optionalField(Codec.FLOAT, "strength", 1.0F).forGetter(ToggleNightVisionConfiguration::strength)
	).apply(instance, ToggleNightVisionConfiguration::new));
}
