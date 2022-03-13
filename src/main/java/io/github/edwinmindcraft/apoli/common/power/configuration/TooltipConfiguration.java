package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record TooltipConfiguration(@Nullable ConfiguredItemCondition<?, ?> itemCondition, ListConfiguration<Component> components) implements IDynamicFeatureConfiguration {
	public static final Codec<TooltipConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredItemCondition.CODEC, "item_condition").forGetter(x -> Optional.ofNullable(x.itemCondition())),
			ListConfiguration.mapCodec(SerializableDataTypes.TEXT, "text", "texts").forGetter(TooltipConfiguration::components)
	).apply(instance, (condition, texts) -> new TooltipConfiguration(condition.orElse(null), texts)));
}
