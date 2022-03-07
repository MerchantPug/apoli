package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ActionOnCallbackConfiguration(@Nullable ConfiguredEntityAction<?, ?> entityActionRespawned,
											@Nullable ConfiguredEntityAction<?, ?> entityActionRemoved,
											@Nullable ConfiguredEntityAction<?, ?> entityActionGained,
											@Nullable ConfiguredEntityAction<?, ?> entityActionLost,
											@Nullable ConfiguredEntityAction<?, ?> entityActionAdded) implements IDynamicFeatureConfiguration {
	public static final Codec<ActionOnCallbackConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action_respawned").forGetter(x -> Optional.ofNullable(x.entityActionRespawned())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action_removed").forGetter(x -> Optional.ofNullable(x.entityActionRemoved())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action_gained").forGetter(x -> Optional.ofNullable(x.entityActionGained())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action_lost").forGetter(x -> Optional.ofNullable(x.entityActionLost())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action_added").forGetter(x -> Optional.ofNullable(x.entityActionAdded()))
	).apply(instance, (cea, cea2, cea3, cea4, cea5) ->
			new ActionOnCallbackConfiguration(cea.orElse(null), cea2.orElse(null), cea3.orElse(null), cea4.orElse(null), cea5.orElse(null))));
}
