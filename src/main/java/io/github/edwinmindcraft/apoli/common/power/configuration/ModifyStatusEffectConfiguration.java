package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import net.minecraft.world.effect.MobEffect;

public record ModifyStatusEffectConfiguration(ListConfiguration<MobEffect> effects,
											  ListConfiguration<ConfiguredModifier<?>> modifiers) implements IValueModifyingPowerConfiguration {

	public static final Codec<ModifyStatusEffectConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.mapCodec(SerializableDataTypes.STATUS_EFFECT, "status_effect", "status_effects").forGetter(ModifyStatusEffectConfiguration::effects),
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyStatusEffectConfiguration::modifiers)
	).apply(instance, ModifyStatusEffectConfiguration::new));
}
