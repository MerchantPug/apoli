package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.world.effect.MobEffectInstance;

public record SpawnEffectCloudConfiguration(float radius, float radiusOnUse, int waitTime,
											ListConfiguration<MobEffectInstance> effects) implements IDynamicFeatureConfiguration {

	public static final Codec<SpawnEffectCloudConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.optionalFieldOf("radius", 3.0F).forGetter(x -> x.radius),
			Codec.FLOAT.optionalFieldOf("radius_on_use", -0.5F).forGetter(x -> x.radiusOnUse),
			Codec.INT.optionalFieldOf("wait_time", 10).forGetter(x -> x.waitTime),
			ListConfiguration.mapCodec(SerializableDataTypes.STATUS_EFFECT_INSTANCE, "effect", "effects").forGetter(x -> x.effects)
	).apply(instance, SpawnEffectCloudConfiguration::new));
}
