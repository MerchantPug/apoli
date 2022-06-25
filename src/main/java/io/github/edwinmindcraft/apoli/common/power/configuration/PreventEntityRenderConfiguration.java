package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import net.minecraft.core.Holder;

public record PreventEntityRenderConfiguration(Holder<ConfiguredEntityCondition<?, ?>> entityCondition,
											   Holder<ConfiguredBiEntityCondition<?, ?>> biEntityCondition) implements IDynamicFeatureConfiguration {
	public static final Codec<PreventEntityRenderConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredEntityCondition.optional("entity_condition").forGetter(PreventEntityRenderConfiguration::entityCondition),
			ConfiguredBiEntityCondition.optional("bientity_condition").forGetter(PreventEntityRenderConfiguration::biEntityCondition)
	).apply(instance, PreventEntityRenderConfiguration::new));
}
