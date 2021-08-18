package dev.experimental.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.world.entity.EquipmentSlot;

public record EquippedItemConfiguration(EquipmentSlot slot,
										ConfiguredItemAction<?, ?> action) implements IDynamicFeatureConfiguration {

	public static final Codec<EquippedItemConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.EQUIPMENT_SLOT.fieldOf("equipment_slot").forGetter(EquippedItemConfiguration::slot),
			ConfiguredItemAction.CODEC.fieldOf("action").forGetter(EquippedItemConfiguration::action)
	).apply(instance, EquippedItemConfiguration::new));
}
