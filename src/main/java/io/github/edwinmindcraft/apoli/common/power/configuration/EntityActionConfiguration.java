package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import net.minecraft.core.Holder;

public record EntityActionConfiguration(
		Holder<ConfiguredEntityAction<?, ?>> entityAction) implements IDynamicFeatureConfiguration {
	public static final Codec<EntityActionConfiguration> CODEC = ConfiguredEntityAction.optional("entity_action")
			.xmap(EntityActionConfiguration::new, EntityActionConfiguration::entityAction).codec();
}
