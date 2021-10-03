package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ModifyJumpConfiguration(ListConfiguration<AttributeModifier> modifiers,
									  @Nullable ConfiguredEntityAction<?, ?> condition) implements IValueModifyingPowerConfiguration {

	public ModifyJumpConfiguration(AttributeModifier... modifiers) {
		this(ListConfiguration.of(modifiers), null);
	}

	public static final Codec<ModifyJumpConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyJumpConfiguration::modifiers),
			ConfiguredEntityAction.CODEC.optionalFieldOf("entity_action").forGetter(x -> Optional.ofNullable(x.condition()))
	).apply(instance, (t1, t2) -> new ModifyJumpConfiguration(t1, t2.orElse(null))));
}
