package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ModifyDamageTakenConfiguration(ListConfiguration<AttributeModifier> modifiers,
											 @Nullable ConfiguredDamageCondition<?, ?> damageCondition,
											 @Nullable ConfiguredEntityAction<?, ?> selfAction,
											 @Nullable ConfiguredEntityAction<?, ?> targetAction) implements IValueModifyingPowerConfiguration {

	public ModifyDamageTakenConfiguration(AttributeModifier... modifiers) {
		this(ListConfiguration.of(modifiers), null, null, null);
	}

	public static final Codec<ModifyDamageTakenConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyDamageTakenConfiguration::modifiers),
			CalioCodecHelper.optionalField(ConfiguredDamageCondition.CODEC, "damage_condition").forGetter(x -> Optional.ofNullable(x.damageCondition())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "self_action").forGetter(x -> Optional.ofNullable(x.selfAction())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "attacker_action").forGetter(x -> Optional.ofNullable(x.targetAction()))
	).apply(instance, (t1, t2, t3, t4) -> new ModifyDamageTakenConfiguration(t1, t2.orElse(null), t3.orElse(null), t4.orElse(null))));
}
