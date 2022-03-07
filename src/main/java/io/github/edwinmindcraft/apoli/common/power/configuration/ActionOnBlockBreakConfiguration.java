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

public record ActionOnBlockBreakConfiguration(@Nullable ConfiguredBlockCondition<?, ?> blockCondition,
											  @Nullable ConfiguredEntityAction<?, ?> entityAction,
											  @Nullable ConfiguredBlockAction<?, ?> blockAction,
											  boolean onlyWhenHarvested) implements IDynamicFeatureConfiguration {

	public static final Codec<ActionOnBlockBreakConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredBlockCondition.CODEC, "block_condition").forGetter(x -> Optional.ofNullable(x.blockCondition())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action").forGetter(x -> Optional.ofNullable(x.entityAction())),
			CalioCodecHelper.optionalField(ConfiguredBlockAction.CODEC, "block_action").forGetter(x -> Optional.ofNullable(x.blockAction())),
			CalioCodecHelper.optionalField(Codec.BOOL, "only_when_harvested", true).forGetter(ActionOnBlockBreakConfiguration::onlyWhenHarvested)
	).apply(instance, (bc, ea, ba, owh) -> new ActionOnBlockBreakConfiguration(bc.orElse(null), ea.orElse(null), ba.orElse(null), owh)));
}
