package io.github.edwinmindcraft.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.util.Comparison;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import io.github.edwinmindcraft.calio.api.network.OptionalFuncs;
import org.jetbrains.annotations.Nullable;

public record IntComparingBiEntityConfiguration(IntegerComparisonConfiguration comparison,
												@Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition) implements IDynamicFeatureConfiguration {
	public static final Codec<IntComparingBiEntityConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			IntegerComparisonConfiguration.withDefaults(Comparison.GREATER_THAN_OR_EQUAL, 1).forGetter(IntComparingBiEntityConfiguration::comparison),
			CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition").forGetter(OptionalFuncs.opt(IntComparingBiEntityConfiguration::biEntityCondition))
	).apply(instance, (t1, t2) -> new IntComparingBiEntityConfiguration(t1, t2.orElse(null))));
}
