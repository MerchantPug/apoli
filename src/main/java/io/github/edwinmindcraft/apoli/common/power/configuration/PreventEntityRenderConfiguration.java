package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record PreventEntityRenderConfiguration(@Nullable ConfiguredEntityCondition<?, ?> entityCondition,
											   @Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition) implements IDynamicFeatureConfiguration {
	public static final Codec<PreventEntityRenderConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredEntityCondition.CODEC, "entity_condition").forGetter(x -> Optional.ofNullable(x.entityCondition())),
			CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition").forGetter(x -> Optional.ofNullable(x.biEntityCondition()))
	).apply(instance, (t1, t2) -> new PreventEntityRenderConfiguration(t1.orElse(null), t2.orElse(null))));
}
