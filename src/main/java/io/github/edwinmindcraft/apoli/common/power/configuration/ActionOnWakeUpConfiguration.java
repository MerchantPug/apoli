package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ActionOnWakeUpConfiguration(@Nullable ConfiguredBlockCondition<?, ?> blockCondition,
										  @Nullable ConfiguredEntityAction<?, ?> entityAction,
										  @Nullable ConfiguredBlockAction<?, ?> blockAction) implements IDynamicFeatureConfiguration {

	public static final Codec<ActionOnWakeUpConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredBlockCondition.CODEC, "block_condition").forGetter(x -> Optional.ofNullable(x.blockCondition())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action").forGetter(x -> Optional.ofNullable(x.entityAction())),
			CalioCodecHelper.optionalField(ConfiguredBlockAction.CODEC, "block_action").forGetter(x -> Optional.ofNullable(x.blockAction()))
	).apply(instance, (bc, ea, ba) -> new ActionOnWakeUpConfiguration(bc.orElse(null), ea.orElse(null), ba.orElse(null))));
}
