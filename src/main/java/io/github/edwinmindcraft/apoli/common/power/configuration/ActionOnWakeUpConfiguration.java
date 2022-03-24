package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import net.minecraft.core.Holder;

public record ActionOnWakeUpConfiguration(Holder<ConfiguredBlockCondition<?, ?>> blockCondition,
										  Holder<ConfiguredEntityAction<?, ?>> entityAction,
										  Holder<ConfiguredBlockAction<?, ?>> blockAction) implements IDynamicFeatureConfiguration {

	public static final Codec<ActionOnWakeUpConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredBlockCondition.optional("block_condition").forGetter(ActionOnWakeUpConfiguration::blockCondition),
			ConfiguredEntityAction.optional("entity_action").forGetter(ActionOnWakeUpConfiguration::entityAction),
			ConfiguredBlockAction.optional("block_action").forGetter(ActionOnWakeUpConfiguration::blockAction)
	).apply(instance, ActionOnWakeUpConfiguration::new));
}
