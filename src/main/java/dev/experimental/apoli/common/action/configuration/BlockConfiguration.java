package dev.experimental.apoli.common.action.configuration;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record BlockConfiguration(Block block) implements IDynamicFeatureConfiguration {

	public static Codec<BlockConfiguration> codec(String name) {
		return SerializableDataTypes.BLOCK.fieldOf(name).xmap(BlockConfiguration::new, BlockConfiguration::block).codec();
	}

	public BlockConfiguration(Block block) {
		this.block = block;
	}

	@Override
	public @NotNull List<String> getWarnings(@NotNull MinecraftServer server) {
		if (this.block() == null)
			return ImmutableList.of("Block/Missing block");
		return IDynamicFeatureConfiguration.super.getWarnings(server);
	}

	@Override
	public boolean isConfigurationValid() {
		return this.block() != null;
	}
}
