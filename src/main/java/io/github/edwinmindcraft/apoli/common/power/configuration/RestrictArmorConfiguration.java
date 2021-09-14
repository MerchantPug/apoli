package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Optional;

public record RestrictArmorConfiguration(Map<EquipmentSlot, ConfiguredItemCondition<?, ?>> conditions,
										 int tickRate) implements IDynamicFeatureConfiguration {
	private static final MapCodec<Map<EquipmentSlot, ConfiguredItemCondition<?, ?>>> EQUIPMENT_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ConfiguredItemCondition.CODEC.optionalFieldOf("head").forGetter(x -> Optional.ofNullable(x.get(EquipmentSlot.HEAD))),
			ConfiguredItemCondition.CODEC.optionalFieldOf("chest").forGetter(x -> Optional.ofNullable(x.get(EquipmentSlot.CHEST))),
			ConfiguredItemCondition.CODEC.optionalFieldOf("legs").forGetter(x -> Optional.ofNullable(x.get(EquipmentSlot.LEGS))),
			ConfiguredItemCondition.CODEC.optionalFieldOf("feet").forGetter(x -> Optional.ofNullable(x.get(EquipmentSlot.FEET)))
	).apply(instance, (head, chest, legs, feet) -> {
		ImmutableMap.Builder<EquipmentSlot, ConfiguredItemCondition<?, ?>> builder = ImmutableMap.builder();
		head.ifPresent(x -> builder.put(EquipmentSlot.HEAD, x));
		chest.ifPresent(x -> builder.put(EquipmentSlot.CHEST, x));
		legs.ifPresent(x -> builder.put(EquipmentSlot.LEGS, x));
		feet.ifPresent(x -> builder.put(EquipmentSlot.FEET, x));
		return builder.build();
	}));

	public static final Codec<RestrictArmorConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			EQUIPMENT_MAP_CODEC.forGetter(RestrictArmorConfiguration::conditions),
			Codec.INT.optionalFieldOf("tick_rate", 80).forGetter(RestrictArmorConfiguration::tickRate)
	).apply(instance, RestrictArmorConfiguration::new));

	public boolean check(EquipmentSlot slot, ItemStack stack) {
		return ConfiguredItemCondition.check(this.conditions.get(slot), stack);
	}

	public void dropIllegalItems(LivingEntity entity) {
		this.conditions().forEach((slot, predicate) -> {
			if (predicate == null) return;
			ItemStack equippedItem = entity.getItemBySlot(slot);
			if (!equippedItem.isEmpty() && !predicate.check(equippedItem)) {
				if (entity instanceof Player ple) {
					if (!ple.getInventory().add(equippedItem))
						ple.drop(equippedItem, true);
				} else
					entity.spawnAtLocation(equippedItem);
				entity.setItemSlot(slot, ItemStack.EMPTY);
			}
		});
	}
}
