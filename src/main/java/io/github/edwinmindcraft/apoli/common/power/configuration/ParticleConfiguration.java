package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ParticleConfiguration(ParticleType<?> particle,
									int frequency) implements IDynamicFeatureConfiguration {

	public static final Codec<ParticleConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.PARTICLE_TYPE.fieldOf("particle").forGetter(ParticleConfiguration::particle),
			Codec.intRange(1, Integer.MAX_VALUE).fieldOf("frequency").forGetter(ParticleConfiguration::frequency)
	).apply(instance, ParticleConfiguration::new));

	@Override
	@NotNull
	public List<String> getWarnings(@NotNull MinecraftServer server) {
		ImmutableList.Builder<String> builder = ImmutableList.builder();
		if (this.particle() == null) builder.add("Optional particle was missing.");
		return ImmutableList.of();
	}

	@Override
	public boolean isConfigurationValid() {
		return this.particle() instanceof ParticleOptions;
	}
}
