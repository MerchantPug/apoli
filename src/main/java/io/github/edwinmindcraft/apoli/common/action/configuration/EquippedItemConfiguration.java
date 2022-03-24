package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.MustBeBound;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;

public record EquippedItemConfiguration(EquipmentSlot slot,
										@MustBeBound Holder<ConfiguredItemAction<?, ?>> action) implements IDynamicFeatureConfiguration {

	public static final Codec<EquippedItemConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.EQUIPMENT_SLOT.fieldOf("equipment_slot").forGetter(EquippedItemConfiguration::slot),
			ConfiguredItemAction.required("action").forGetter(EquippedItemConfiguration::action)
	).apply(instance, EquippedItemConfiguration::new));
}
