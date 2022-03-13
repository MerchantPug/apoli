package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record BiEntityConditionConfiguration(@Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition) implements IDynamicFeatureConfiguration {
	public static final Codec<BiEntityConditionConfiguration> CODEC = CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition")
			.xmap(x -> new BiEntityConditionConfiguration(x.orElse(null)), x -> Optional.ofNullable(x.biEntityCondition())).codec();
}
