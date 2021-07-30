package dev.experimental.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.configuration.ListConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityAction;
import dev.experimental.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ModifyJumpConfiguration(ListConfiguration<EntityAttributeModifier> modifiers,
									  @Nullable ConfiguredEntityAction<?, ?> condition) implements IValueModifyingPowerConfiguration {
	public static final Codec<ModifyJumpConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyJumpConfiguration::modifiers),
			ConfiguredEntityAction.CODEC.optionalFieldOf("entity_action").forGetter(x -> Optional.ofNullable(x.condition()))
	).apply(instance, (t1, t2) -> new ModifyJumpConfiguration(t1, t2.orElse(null))));
}
