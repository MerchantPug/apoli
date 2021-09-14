package io.github.edwinmindcraft.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.world.entity.EquipmentSlot;

public record EquippedItemConfiguration(EquipmentSlot slot,
										ConfiguredItemCondition<?, ?> condition) implements IDynamicFeatureConfiguration {
	public static final Codec<EquippedItemConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.EQUIPMENT_SLOT.fieldOf("equipment_slot").forGetter(EquippedItemConfiguration::slot),
			ConfiguredItemCondition.CODEC.fieldOf("item_condition").forGetter(EquippedItemConfiguration::condition)
	).apply(instance, EquippedItemConfiguration::new));
}
