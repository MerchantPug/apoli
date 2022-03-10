package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.*;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ModifyDamageDealtConfiguration(ListConfiguration<AttributeModifier> modifiers,
											 @Nullable ConfiguredDamageCondition<?, ?> damageCondition,
											 @Nullable ConfiguredEntityCondition<?, ?> targetCondition,
											 @Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition,
											 @Nullable ConfiguredEntityAction<?, ?> selfAction,
											 @Nullable ConfiguredEntityAction<?, ?> targetAction,
											 @Nullable ConfiguredBiEntityAction<?, ?> biEntityAction) implements IValueModifyingPowerConfiguration {

	public ModifyDamageDealtConfiguration(AttributeModifier... modifiers) {
		this(ListConfiguration.of(modifiers), null, null, null, null, null, null);
	}

	public static final Codec<ModifyDamageDealtConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyDamageDealtConfiguration::modifiers),
			CalioCodecHelper.optionalField(ConfiguredDamageCondition.CODEC, "damage_condition").forGetter(x -> Optional.ofNullable(x.damageCondition())),
			CalioCodecHelper.optionalField(ConfiguredEntityCondition.CODEC, "target_condition").forGetter(x -> Optional.ofNullable(x.targetCondition())),
			CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition").forGetter(x -> Optional.ofNullable(x.biEntityCondition())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "self_action").forGetter(x -> Optional.ofNullable(x.selfAction())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "target_action").forGetter(x -> Optional.ofNullable(x.targetAction())),
			CalioCodecHelper.optionalField(ConfiguredBiEntityAction.CODEC, "bientity_action").forGetter(x -> Optional.ofNullable(x.biEntityAction()))
	).apply(instance, (t1, t2, t3, t4, t5, t6, t7) -> new ModifyDamageDealtConfiguration(t1, t2.orElse(null), t3.orElse(null), t4.orElse(null), t5.orElse(null), t6.orElse(null), t7.orElse(null))));
}
