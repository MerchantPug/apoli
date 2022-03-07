package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ActionOnItemUseConfiguration(@Nullable ConfiguredItemCondition<?, ?> itemCondition,
										   @Nullable ConfiguredEntityAction<?, ?> entityAction,
										   @Nullable ConfiguredItemAction<?, ?> itemAction) implements IDynamicFeatureConfiguration {

	public static final Codec<ActionOnItemUseConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredItemCondition.CODEC, "item_condition").forGetter(x -> Optional.ofNullable(x.itemCondition())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action").forGetter(x -> Optional.ofNullable(x.entityAction())),
			CalioCodecHelper.optionalField(ConfiguredItemAction.CODEC, "item_action").forGetter(x -> Optional.ofNullable(x.itemAction()))
	).apply(instance, (o1, o2, o3) -> new ActionOnItemUseConfiguration(o1.orElse(null), o2.orElse(null), o3.orElse(null))));
}
