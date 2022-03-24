package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.*;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ModifyDamageDealtConfiguration(ListConfiguration<AttributeModifier> modifiers,
											 Holder<ConfiguredDamageCondition<?,?>> damageCondition,
											 Holder<ConfiguredEntityCondition<?,?>> targetCondition,
											 Holder<ConfiguredBiEntityCondition<?,?>> biEntityCondition,
											 Holder<ConfiguredEntityAction<?,?>> selfAction,
											 Holder<ConfiguredEntityAction<?,?>> targetAction,
											 Holder<ConfiguredBiEntityAction<?, ?>> biEntityAction) implements IValueModifyingPowerConfiguration {

	public ModifyDamageDealtConfiguration(AttributeModifier... modifiers) {
		this(ListConfiguration.of(modifiers), null, null, null, null, null, null);
	}

	public static final Codec<ModifyDamageDealtConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyDamageDealtConfiguration::modifiers),
			ConfiguredDamageCondition.optional("damage_condition").forGetter(ModifyDamageDealtConfiguration::damageCondition),
			ConfiguredEntityCondition.optional("target_condition").forGetter(ModifyDamageDealtConfiguration::targetCondition),
			ConfiguredBiEntityCondition.optional("bientity_condition").forGetter(ModifyDamageDealtConfiguration::biEntityCondition),
			ConfiguredEntityAction.optional("self_action").forGetter(ModifyDamageDealtConfiguration::selfAction),
			ConfiguredEntityAction.optional("target_action").forGetter(ModifyDamageDealtConfiguration::targetAction),
			ConfiguredBiEntityAction.optional("bientity_action").forGetter(ModifyDamageDealtConfiguration::biEntityAction)
	).apply(instance, ModifyDamageDealtConfiguration::new));
}
