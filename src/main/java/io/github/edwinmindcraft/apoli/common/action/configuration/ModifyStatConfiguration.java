
package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import net.minecraft.core.Holder;
import net.minecraft.stats.Stat;

public record ModifyStatConfiguration(Stat<?> stat,
                                      Holder<ConfiguredModifier<?>> modifier) implements IDynamicFeatureConfiguration {

	public static final Codec<ModifyStatConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.STAT.fieldOf("stat").forGetter(ModifyStatConfiguration::stat),
			ConfiguredModifier.required("modifier").forGetter(ModifyStatConfiguration::modifier)
	).apply(instance, ModifyStatConfiguration::new));
}
