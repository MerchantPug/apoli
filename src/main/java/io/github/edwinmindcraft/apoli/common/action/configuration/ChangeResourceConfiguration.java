package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ChangeResourceConfiguration(ResourceLocation resource,
										  int amount,
										  ResourceOperation operation) implements IDynamicFeatureConfiguration {

	public static final Codec<ChangeResourceConfiguration> ANY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.IDENTIFIER.fieldOf("resource").forGetter(ChangeResourceConfiguration::resource),
			Codec.INT.fieldOf("change").forGetter(ChangeResourceConfiguration::amount),
			CalioCodecHelper.optionalField(ApoliDataTypes.RESOURCE_OPERATION, "operation", ResourceOperation.ADD).forGetter(ChangeResourceConfiguration::operation)
	).apply(instance, ChangeResourceConfiguration::new));


	public static final Codec<ChangeResourceConfiguration> SET_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.IDENTIFIER.fieldOf("resource").forGetter(ChangeResourceConfiguration::resource),
			Codec.INT.fieldOf("value").forGetter(ChangeResourceConfiguration::amount)
	).apply(instance, (ResourceLocation resource, Integer amount) -> new ChangeResourceConfiguration(resource, amount, ResourceOperation.SET)));


	@Override
	public @NotNull List<String> getErrors(@NotNull ICalioDynamicRegistryManager server) {
		return this.checkPower(server, this.resource()).stream().map(x -> "Missing power: %s".formatted(x.toString())).toList();
	}

	@Override
	public @NotNull List<String> getWarnings(@NotNull ICalioDynamicRegistryManager server) {
		if (this.amount() == 0 && this.operation() == ResourceOperation.ADD)
			return ImmutableList.of("Change expected, was 0");
		return ImmutableList.of();
	}

	@Override
	public boolean isConfigurationValid() {
		return this.amount() != 0;
	}
}
