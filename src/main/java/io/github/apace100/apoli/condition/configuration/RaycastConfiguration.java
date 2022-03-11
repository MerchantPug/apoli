package io.github.apace100.apoli.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.configuration.RaycastSettingsConfiguration;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record RaycastConfiguration(RaycastSettingsConfiguration settings,
								   @Nullable ConfiguredBiEntityCondition<?, ?> matchCondition,
								   @Nullable ConfiguredBiEntityCondition<?, ?> hitCondition,
								   @Nullable ConfiguredBlockCondition<?, ?> blockCondition) implements IDynamicFeatureConfiguration {
	public static final Codec<RaycastConfiguration> CODEC = RecordCodecBuilder.create(instance-> instance.group(
			RaycastSettingsConfiguration.MAP_CODEC.forGetter(RaycastConfiguration::settings),
			CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "match_bientity_condition").forGetter(x -> Optional.ofNullable(x.matchCondition())),
			CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "hit_bientity_condition").forGetter(x -> Optional.ofNullable(x.matchCondition())),
			CalioCodecHelper.optionalField(ConfiguredBlockCondition.CODEC, "block_condition").forGetter(x -> Optional.ofNullable(x.blockCondition()))
	).apply(instance, (t1, t2, t3, t4) -> new RaycastConfiguration(t1, t2.orElse(null), t3.orElse(null), t4.orElse(null))));
}
