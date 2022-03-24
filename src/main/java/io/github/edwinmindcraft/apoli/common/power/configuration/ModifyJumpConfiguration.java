package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ModifyJumpConfiguration(ListConfiguration<AttributeModifier> modifiers,
									  Holder<ConfiguredEntityAction<?,?>> condition) implements IValueModifyingPowerConfiguration {

	public ModifyJumpConfiguration(AttributeModifier... modifiers) {
		this(ListConfiguration.of(modifiers), null);
	}

	public static final Codec<ModifyJumpConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyJumpConfiguration::modifiers),
			ConfiguredEntityAction.optional("entity_action").forGetter(ModifyJumpConfiguration::condition)
	).apply(instance, ModifyJumpConfiguration::new));
}
