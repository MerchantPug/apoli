package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ActionOverItemConfiguration(@Nullable ConfiguredEntityAction<?, ?> entityAction,
										  @Nullable ConfiguredEntityAction<?, ?> risingAction,
										  @Nullable ConfiguredEntityAction<?, ?> fallingAction,
										  int interval) implements IDynamicFeatureConfiguration {
	public static final Codec<ActionOverItemConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action").forGetter(x -> Optional.ofNullable(x.entityAction())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "rising_action").forGetter(x -> Optional.ofNullable(x.risingAction())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "falling_action").forGetter(x -> Optional.ofNullable(x.fallingAction())),
			Codec.INT.fieldOf("interval").forGetter(ActionOverItemConfiguration::interval)
	).apply(instance, (cea, cea2, cea3, interval) ->
			new ActionOverItemConfiguration(cea.orElse(null), cea2.orElse(null), cea3.orElse(null), interval)));
}
