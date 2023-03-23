package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.*;
import io.github.edwinmindcraft.apoli.common.power.ModifyGrindstonePower;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record ModifyGrindstoneConfiguration(Holder<ConfiguredItemCondition<?, ?>> topItemCondition, Holder<ConfiguredItemCondition<?, ?>> bottomItemCondition,
											Holder<ConfiguredItemCondition<?, ?>> outputItemCondition, Holder<ConfiguredBlockCondition<?, ?>> blockCondition,
											Optional<ItemStack> resultStack, Holder<ConfiguredItemAction<?, ?>> resultItemAction,
											Holder<ConfiguredItemAction<?, ?>> lateItemAction, Holder<ConfiguredEntityAction<?, ?>> entityAction,
											Holder<ConfiguredBlockAction<?, ?>> blockAction, ModifyGrindstonePower.ResultType resultType,
											Optional<ConfiguredModifier<?>> experienceModifier) implements IDynamicFeatureConfiguration {
	public static final Codec<ModifyGrindstoneConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredItemCondition.optional("top_condition").forGetter(ModifyGrindstoneConfiguration::topItemCondition),
			ConfiguredItemCondition.optional("bottom_condition").forGetter(ModifyGrindstoneConfiguration::bottomItemCondition),
			ConfiguredItemCondition.optional("output_condition").forGetter(ModifyGrindstoneConfiguration::outputItemCondition),
			ConfiguredBlockCondition.optional("block_condition").forGetter(ModifyGrindstoneConfiguration::blockCondition),
			CalioCodecHelper.optionalField(SerializableDataTypes.ITEM_STACK, "result_stack").forGetter(ModifyGrindstoneConfiguration::resultStack),
			ConfiguredItemAction.optional("item_action").forGetter(ModifyGrindstoneConfiguration::resultItemAction),
			ConfiguredItemAction.optional("item_action_after_grinding").forGetter(ModifyGrindstoneConfiguration::lateItemAction),
			ConfiguredEntityAction.optional("entity_action").forGetter(ModifyGrindstoneConfiguration::entityAction),
			ConfiguredBlockAction.optional("block_action").forGetter(ModifyGrindstoneConfiguration::blockAction),
			CalioCodecHelper.optionalField(SerializableDataType.enumValue(ModifyGrindstonePower.ResultType.class), "result_type", ModifyGrindstonePower.ResultType.UNCHANGED).forGetter(ModifyGrindstoneConfiguration::resultType),
			CalioCodecHelper.optionalField(ConfiguredModifier.CODEC, "xp_modifier").forGetter(ModifyGrindstoneConfiguration::experienceModifier)
	).apply(instance, ModifyGrindstoneConfiguration::new));

}
