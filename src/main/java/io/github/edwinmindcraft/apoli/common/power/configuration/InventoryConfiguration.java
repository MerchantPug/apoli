package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record InventoryConfiguration(String inventoryName, boolean dropOnDeath,
									 @Nullable ConfiguredItemCondition<?, ?> dropFilter,
									 IActivePower.Key key) implements IDynamicFeatureConfiguration {
	public static final Codec<InventoryConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(Codec.STRING, "name", "container.inventory").forGetter(InventoryConfiguration::inventoryName),
			CalioCodecHelper.optionalField(Codec.BOOL, "drop_on_death", false).forGetter(InventoryConfiguration::dropOnDeath),
			CalioCodecHelper.optionalField(ConfiguredItemCondition.CODEC, "drop_on_death_filter").forGetter(x -> Optional.ofNullable(x.dropFilter())),
			CalioCodecHelper.optionalField(IActivePower.Key.BACKWARD_COMPATIBLE_CODEC, "key", IActivePower.Key.PRIMARY).forGetter(InventoryConfiguration::key)
	).apply(instance, (t1, t2, t3, t4) -> new InventoryConfiguration(t1, t2, t3.orElse(null), t4)));
}
