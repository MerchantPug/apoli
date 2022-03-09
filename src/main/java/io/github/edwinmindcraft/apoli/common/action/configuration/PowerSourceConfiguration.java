package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.PowerReference;
import net.minecraft.resources.ResourceLocation;

public record PowerSourceConfiguration(PowerReference power, ResourceLocation source) implements IDynamicFeatureConfiguration {
	public static final Codec<PowerSourceConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			PowerReference.mapCodec("power").forGetter(PowerSourceConfiguration::power),
			SerializableDataTypes.IDENTIFIER.fieldOf("source").forGetter(PowerSourceConfiguration::source)
	).apply(instance, PowerSourceConfiguration::new));
}
