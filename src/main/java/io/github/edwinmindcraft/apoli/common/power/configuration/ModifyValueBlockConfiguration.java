package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ModifyValueBlockConfiguration(ListConfiguration<AttributeModifier> modifiers,
											Holder<ConfiguredBlockCondition<?, ?>> condition) implements IValueModifyingPowerConfiguration {
	public static final Codec<ModifyValueBlockConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyValueBlockConfiguration::modifiers),
			ConfiguredBlockCondition.optional("block_condition").forGetter(ModifyValueBlockConfiguration::condition)
	).apply(instance, ModifyValueBlockConfiguration::new));
}
