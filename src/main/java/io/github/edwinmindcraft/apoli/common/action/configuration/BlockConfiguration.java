package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record BlockConfiguration(Block block) implements IDynamicFeatureConfiguration {

	public static Codec<BlockConfiguration> codec(String name) {
		return SerializableDataTypes.BLOCK.fieldOf(name).xmap(BlockConfiguration::new, BlockConfiguration::block).codec();
	}

	@Override
	public @NotNull List<String> getWarnings(@NotNull ICalioDynamicRegistryManager server) {
		if (this.block() == null)
			return ImmutableList.of("Block/Missing block");
		return IDynamicFeatureConfiguration.super.getWarnings(server);
	}

	@Override
	public boolean isConfigurationValid() {
		return this.block() != null;
	}
}
