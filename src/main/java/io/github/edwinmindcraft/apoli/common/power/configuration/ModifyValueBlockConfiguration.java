package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record ModifyValueBlockConfiguration(ListConfiguration<ConfiguredModifier<?>> modifiers,
											Holder<ConfiguredBlockCondition<?, ?>> condition) implements IValueModifyingPowerConfiguration {
	public static final Codec<ModifyValueBlockConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyValueBlockConfiguration::modifiers),
			ConfiguredBlockCondition.optional("block_condition").forGetter(ModifyValueBlockConfiguration::condition)
	).apply(instance, ModifyValueBlockConfiguration::new));
}
