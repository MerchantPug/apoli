package io.github.apace100.apoli.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.configuration.RaycastSettingsConfiguration;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import net.minecraft.core.Holder;

public record RaycastConfiguration(RaycastSettingsConfiguration settings,
								   Holder<ConfiguredBiEntityCondition<?, ?>> matchCondition,
								   Holder<ConfiguredBiEntityCondition<?, ?>> hitCondition,
								   Holder<ConfiguredBlockCondition<?, ?>> blockCondition) implements IDynamicFeatureConfiguration {
	public static final Codec<RaycastConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			RaycastSettingsConfiguration.MAP_CODEC.forGetter(RaycastConfiguration::settings),
			ConfiguredBiEntityCondition.optional("match_bientity_condition").forGetter(RaycastConfiguration::matchCondition),
			ConfiguredBiEntityCondition.optional("hit_bientity_condition").forGetter(RaycastConfiguration::hitCondition),
			ConfiguredBlockCondition.optional("block_condition").forGetter(RaycastConfiguration::blockCondition)
	).apply(instance, RaycastConfiguration::new));
}
