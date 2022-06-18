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

public record ModifyHarvestConfiguration(ListConfiguration<AttributeModifier> modifiers,
										 Holder<ConfiguredBlockCondition<?,?>> condition,
										 boolean allow) implements IValueModifyingPowerConfiguration {

	public static final Codec<ModifyHarvestConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyHarvestConfiguration::modifiers),
			ConfiguredBlockCondition.optional("block_condition").forGetter(ModifyHarvestConfiguration::condition),
			CalioCodecHelper.BOOL.fieldOf("allow").forGetter(ModifyHarvestConfiguration::allow)
	).apply(instance, ModifyHarvestConfiguration::new));
}
