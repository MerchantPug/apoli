package dev.experimental.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.configuration.DoubleComparisonConfiguration;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;

public record FluidTagComparisonConfiguration(DoubleComparisonConfiguration comparison,
											  Tag<Fluid> tag) implements IDynamicFeatureConfiguration {
	public static Codec<FluidTagComparisonConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			DoubleComparisonConfiguration.MAP_CODEC.forGetter(FluidTagComparisonConfiguration::comparison),
			SerializableDataTypes.FLUID_TAG.fieldOf("fluid").forGetter(FluidTagComparisonConfiguration::tag)
	).apply(instance, FluidTagComparisonConfiguration::new));
}
