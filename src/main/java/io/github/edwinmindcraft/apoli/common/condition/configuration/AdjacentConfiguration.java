package io.github.edwinmindcraft.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import net.minecraft.core.Holder;

public record AdjacentConfiguration(IntegerComparisonConfiguration comparison,
									Holder<ConfiguredBlockCondition<?, ?>> condition) implements IDynamicFeatureConfiguration {
	public static final Codec<AdjacentConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			IntegerComparisonConfiguration.MAP_CODEC.forGetter(AdjacentConfiguration::comparison),
			ConfiguredBlockCondition.required("adjacent_condition").forGetter(AdjacentConfiguration::condition)
	).apply(instance, AdjacentConfiguration::new));
}
