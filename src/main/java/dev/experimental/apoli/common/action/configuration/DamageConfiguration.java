package dev.experimental.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.damage.DamageSource;

public record DamageConfiguration(DamageSource source, float amount) implements IDynamicFeatureConfiguration {
	public static final Codec<DamageConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.DAMAGE_SOURCE.fieldOf("source").forGetter(DamageConfiguration::source),
			Codec.FLOAT.fieldOf("amount").forGetter(DamageConfiguration::amount)
	).apply(instance, DamageConfiguration::new));
}
