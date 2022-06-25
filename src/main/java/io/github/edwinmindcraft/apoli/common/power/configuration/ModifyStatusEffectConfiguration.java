package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public record ModifyStatusEffectConfiguration(MobEffect effect,
											  AttributeModifier modifier) implements IValueModifyingPowerConfiguration {

	public static final Codec<ModifyStatusEffectConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			SerializableDataTypes.STATUS_EFFECT.fieldOf("status_effect").forGetter(ModifyStatusEffectConfiguration::effect),
			SerializableDataTypes.ATTRIBUTE_MODIFIER.fieldOf("modifier").forGetter(ModifyStatusEffectConfiguration::modifier)
	).apply(instance, ModifyStatusEffectConfiguration::new));

	@Override
	public @NotNull ListConfiguration<AttributeModifier> modifiers() {
		return ListConfiguration.of(this.modifier());
	}
}
