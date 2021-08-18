package dev.experimental.apoli.common.action.configuration;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.calio.api.CalioAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ChangeResourceConfiguration(ResourceLocation resource,
										  int amount) implements IDynamicFeatureConfiguration {

	public static final Codec<ChangeResourceConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.IDENTIFIER.fieldOf("resource").forGetter(ChangeResourceConfiguration::resource),
			Codec.INT.fieldOf("change").forGetter(ChangeResourceConfiguration::amount)
	).apply(instance, ChangeResourceConfiguration::new));

	@Override
	public @NotNull List<String> getErrors(@NotNull MinecraftServer server) {
		return this.checkPower(CalioAPI.getDynamicRegistries(server), this.resource()).stream().map(x -> "Missing power: %s".formatted(x.toString())).toList();
	}

	@Override
	public @NotNull List<String> getWarnings(@NotNull MinecraftServer server) {
		if (this.amount() == 0)
			return ImmutableList.of("Change expected, was 0");
		return ImmutableList.of();
	}

	@Override
	public boolean isConfigurationValid() {
		return this.amount() != 0;
	}
}
