package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import net.minecraft.core.Holder;

public record PreventDeathConfiguration(Holder<ConfiguredEntityAction<?, ?>> action,
										Holder<ConfiguredDamageCondition<?, ?>> condition) implements IDynamicFeatureConfiguration {
	public static final Codec<PreventDeathConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredEntityAction.optional("entity_action").forGetter(PreventDeathConfiguration::action),
			ConfiguredDamageCondition.optional("damage_condition").forGetter(PreventDeathConfiguration::condition)
	).apply(instance, PreventDeathConfiguration::new));
}
