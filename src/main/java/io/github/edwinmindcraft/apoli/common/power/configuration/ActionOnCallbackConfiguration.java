package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import io.github.edwinmindcraft.calio.api.network.OptionalFuncs;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ActionOnCallbackConfiguration(@Nullable ConfiguredEntityAction<?, ?> entityActionRespawned,
											@Nullable ConfiguredEntityAction<?, ?> entityActionRemoved,
											@Nullable ConfiguredEntityAction<?, ?> entityActionGained,
											@Nullable ConfiguredEntityAction<?, ?> entityActionLost,
											@Nullable ConfiguredEntityAction<?, ?> entityActionAdded) implements IDynamicFeatureConfiguration {
	public static final Codec<ActionOnCallbackConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action_respawned").forGetter(OptionalFuncs.opt(ActionOnCallbackConfiguration::entityActionRespawned)),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action_removed").forGetter(OptionalFuncs.opt(ActionOnCallbackConfiguration::entityActionRemoved)),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action_gained").forGetter(OptionalFuncs.opt(ActionOnCallbackConfiguration::entityActionGained)),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action_lost").forGetter(OptionalFuncs.opt(ActionOnCallbackConfiguration::entityActionLost)),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action_added").forGetter(OptionalFuncs.opt(ActionOnCallbackConfiguration::entityActionAdded))
	).apply(instance, OptionalFuncs.of(ActionOnCallbackConfiguration::new)));
}
