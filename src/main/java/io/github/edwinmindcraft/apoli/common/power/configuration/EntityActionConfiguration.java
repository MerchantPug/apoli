package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import io.github.edwinmindcraft.calio.api.network.OptionalFuncs;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

public record EntityActionConfiguration(Holder<ConfiguredEntityAction<?,?>> entityAction) implements IDynamicFeatureConfiguration {
	public static final Codec<EntityActionConfiguration> CODEC = ConfiguredEntityAction.optional("entity_action")
			.xmap(EntityActionConfiguration::new, EntityActionConfiguration::entityAction).codec();
}
