package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public record ModifyStatusEffectConfiguration(ListConfiguration<MobEffect> effects,
											  ListConfiguration<ConfiguredModifier<?>> modifier) implements IValueModifyingPowerConfiguration {

	public static final Codec<ModifyStatusEffectConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.mapCodec(SerializableDataTypes.STATUS_EFFECT, "status_effect", "status_effects").forGetter(ModifyStatusEffectConfiguration::effects),
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyStatusEffectConfiguration::modifier)
	).apply(instance, ModifyStatusEffectConfiguration::new));

	@Override
	public @NotNull ListConfiguration<ConfiguredModifier<?>> modifiers() {
		return this.modifier();
	}
}
