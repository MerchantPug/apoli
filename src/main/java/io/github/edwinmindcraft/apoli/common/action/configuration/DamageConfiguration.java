package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.damagesource.DamageSource;

public record DamageConfiguration(DamageSource source, float amount) implements IDynamicFeatureConfiguration {
	public static final Codec<DamageConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.DAMAGE_SOURCE.fieldOf("source").forGetter(DamageConfiguration::source),
			CalioCodecHelper.FLOAT.fieldOf("amount").forGetter(DamageConfiguration::amount)
	).apply(instance, DamageConfiguration::new));
}
