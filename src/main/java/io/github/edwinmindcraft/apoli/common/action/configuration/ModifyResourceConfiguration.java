
package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.core.Holder;

public record ModifyResourceConfiguration(Holder<ConfiguredPower<?, ?>> resource,
										  Holder<ConfiguredModifier<?>> modifier) implements IDynamicFeatureConfiguration {

	public static final Codec<ModifyResourceConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredPower.CODEC_SET.holderRef().fieldOf("resource").forGetter(ModifyResourceConfiguration::resource),
			ConfiguredModifier.required("modifier").forGetter(ModifyResourceConfiguration::modifier)
	).apply(instance, ModifyResourceConfiguration::new));
}
