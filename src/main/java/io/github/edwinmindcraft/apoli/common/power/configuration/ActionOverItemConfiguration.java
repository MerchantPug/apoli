package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ActionOverItemConfiguration(Holder<ConfiguredEntityAction<?,?>> entityAction,
										  Holder<ConfiguredEntityAction<?,?>> risingAction,
										  Holder<ConfiguredEntityAction<?,?>> fallingAction,
										  int interval) implements IDynamicFeatureConfiguration {
	public static final Codec<ActionOverItemConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredEntityAction.optional("entity_action").forGetter(ActionOverItemConfiguration::entityAction),
			ConfiguredEntityAction.optional("rising_action").forGetter(ActionOverItemConfiguration::risingAction),
			ConfiguredEntityAction.optional("falling_action").forGetter(ActionOverItemConfiguration::fallingAction),
			Codec.INT.fieldOf("interval").forGetter(ActionOverItemConfiguration::interval)
	).apply(instance, ActionOverItemConfiguration::new));
}
