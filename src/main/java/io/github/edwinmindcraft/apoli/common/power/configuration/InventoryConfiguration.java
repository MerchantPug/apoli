package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record InventoryConfiguration(String inventoryName, boolean dropOnDeath,
									 @Nullable ConfiguredItemCondition<?, ?> dropFilter,
									 IActivePower.Key key) implements IDynamicFeatureConfiguration {
	public static final Codec<InventoryConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.optionalFieldOf("name", "container.inventory").forGetter(InventoryConfiguration::inventoryName),
			Codec.BOOL.optionalFieldOf("drop_on_death", false).forGetter(InventoryConfiguration::dropOnDeath),
			ConfiguredItemCondition.CODEC.optionalFieldOf("drop_on_death_filter").forGetter(x -> Optional.ofNullable(x.dropFilter())),
			IActivePower.Key.BACKWARD_COMPATIBLE_CODEC.optionalFieldOf("key", IActivePower.Key.PRIMARY).forGetter(InventoryConfiguration::key)
	).apply(instance, (t1, t2, t3, t4) -> new InventoryConfiguration(t1, t2, t3.orElse(null), t4)));
}
