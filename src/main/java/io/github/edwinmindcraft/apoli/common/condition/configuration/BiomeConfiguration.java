package io.github.edwinmindcraft.apoli.common.condition.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiomeCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record BiomeConfiguration(ListConfiguration<ResourceKey<Biome>> biomes,
								 @Nullable ConfiguredBiomeCondition<?, ?> condition) implements IDynamicFeatureConfiguration {

	public static final Codec<BiomeConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.mapCodec(CalioCodecHelper.resourceKey(Registry.BIOME_REGISTRY), "biome", "biomes").forGetter(BiomeConfiguration::biomes),
			ConfiguredBiomeCondition.CODEC.optionalFieldOf("condition").forGetter(x -> Optional.ofNullable(x.condition()))
	).apply(instance, (t1, t2) -> new BiomeConfiguration(t1, t2.orElse(null))));
}
