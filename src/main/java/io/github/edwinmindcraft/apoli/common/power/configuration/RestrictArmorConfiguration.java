package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliBuiltinRegistries;
import io.github.edwinmindcraft.apoli.api.registry.ApoliDynamicRegistries;
import io.github.edwinmindcraft.apoli.common.registry.condition.ApoliDefaultConditions;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Optional;

public record RestrictArmorConfiguration(Map<EquipmentSlot, Holder<ConfiguredItemCondition<?, ?>>> conditions,
										 int tickRate) implements IDynamicFeatureConfiguration {
	private static Holder<ConfiguredItemCondition<?, ?>> itemDefault() {
		return ApoliBuiltinRegistries.CONFIGURED_ITEM_CONDITIONS.get().getHolder(ApoliDynamicRegistries.DENY).orElseThrow();
	}

	private static final MapCodec<Map<EquipmentSlot, Holder<ConfiguredItemCondition<?, ?>>>> EQUIPMENT_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ConfiguredItemCondition.optional("head").forGetter(x -> Optional.ofNullable(x.get(EquipmentSlot.HEAD)).orElseGet(RestrictArmorConfiguration::itemDefault)),
			ConfiguredItemCondition.optional("chest").forGetter(x -> Optional.ofNullable(x.get(EquipmentSlot.CHEST)).orElseGet(RestrictArmorConfiguration::itemDefault)),
			ConfiguredItemCondition.optional("legs").forGetter(x -> Optional.ofNullable(x.get(EquipmentSlot.LEGS)).orElseGet(RestrictArmorConfiguration::itemDefault)),
			ConfiguredItemCondition.optional("feet").forGetter(x -> Optional.ofNullable(x.get(EquipmentSlot.FEET)).orElseGet(RestrictArmorConfiguration::itemDefault))
	).apply(instance, (head, chest, legs, feet) -> {
		ImmutableMap.Builder<EquipmentSlot, Holder<ConfiguredItemCondition<?, ?>>> builder = ImmutableMap.builder();
		builder.put(EquipmentSlot.HEAD, head);
		builder.put(EquipmentSlot.CHEST, chest);
		builder.put(EquipmentSlot.LEGS, legs);
		builder.put(EquipmentSlot.FEET, feet);
		return builder.build();
	}));

	public static final Codec<RestrictArmorConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			EQUIPMENT_MAP_CODEC.forGetter(RestrictArmorConfiguration::conditions),
			CalioCodecHelper.optionalField(Codec.INT, "tick_rate", 80).forGetter(RestrictArmorConfiguration::tickRate)
	).apply(instance, RestrictArmorConfiguration::new));

	public boolean check(EquipmentSlot slot, Level level, ItemStack stack) {
		return this.conditions.get(slot) != null && ConfiguredItemCondition.check(this.conditions.get(slot), level, stack);
	}

	public void dropIllegalItems(Entity entity) {
		if (!(entity instanceof LivingEntity living))
			return;
		this.conditions().forEach((slot, predicate) -> {
			ItemStack equippedItem = living.getItemBySlot(slot);
			if (!equippedItem.isEmpty() && ConfiguredItemCondition.check(predicate, living.level, equippedItem)) {
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
