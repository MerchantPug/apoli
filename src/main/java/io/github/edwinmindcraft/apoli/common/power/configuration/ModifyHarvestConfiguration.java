package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ModifyHarvestConfiguration(ListConfiguration<AttributeModifier> modifiers,
										 @Nullable ConfiguredBlockCondition<?, ?> condition,
										 boolean allow) implements IValueModifyingPowerConfiguration {

	public static final Codec<ModifyHarvestConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyHarvestConfiguration::modifiers),
			ConfiguredBlockCondition.CODEC.optionalFieldOf("block_condition").forGetter(x -> Optional.ofNullable(x.condition())),
			Codec.BOOL.fieldOf("allow").forGetter(ModifyHarvestConfiguration::allow)
	).apply(instance, (t1, t2, t3) -> new ModifyHarvestConfiguration(t1, t2.orElse(null), t3)));
}
