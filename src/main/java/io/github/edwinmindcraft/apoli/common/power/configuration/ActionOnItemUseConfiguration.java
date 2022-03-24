package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import io.github.edwinmindcraft.calio.api.network.OptionalFuncs;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ActionOnItemUseConfiguration(Holder<ConfiguredItemCondition<?,?>> itemCondition,
										   Holder<ConfiguredEntityAction<?,?>> entityAction,
										   Holder<ConfiguredItemAction<?,?>> itemAction) implements IDynamicFeatureConfiguration {

	public static final Codec<ActionOnItemUseConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredItemCondition.optional("item_condition").forGetter(ActionOnItemUseConfiguration::itemCondition),
			ConfiguredEntityAction.optional("entity_action").forGetter(ActionOnItemUseConfiguration::entityAction),
			ConfiguredItemAction.optional("item_action").forGetter(ActionOnItemUseConfiguration::itemAction)
	).apply(instance, ActionOnItemUseConfiguration::new));
}
