package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ModifyFoodConfiguration(ListConfiguration<AttributeModifier> foodModifiers,
									  ListConfiguration<AttributeModifier> saturationModifiers,
									  @Nullable ConfiguredItemCondition<?, ?> itemCondition,
									  @Nullable ConfiguredEntityAction<?, ?> entityAction) implements IDynamicFeatureConfiguration {
	public static final Codec<ModifyFoodConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.modifierCodec("food_modifier").forGetter(ModifyFoodConfiguration::foodModifiers),
			ListConfiguration.modifierCodec("saturation_modifier").forGetter(ModifyFoodConfiguration::saturationModifiers),
			CalioCodecHelper.optionalField(ConfiguredItemCondition.CODEC, "item_condition").forGetter(x -> Optional.ofNullable(x.itemCondition())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action").forGetter(x -> Optional.ofNullable(x.entityAction()))
	).apply(instance, (t1, t2, t3, t4) -> new ModifyFoodConfiguration(t1, t2, t3.orElse(null), t4.orElse(null))));
}
