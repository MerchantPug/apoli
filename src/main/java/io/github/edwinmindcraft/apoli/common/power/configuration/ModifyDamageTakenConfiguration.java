package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ModifyDamageTakenConfiguration(ListConfiguration<AttributeModifier> modifiers,
											 Holder<ConfiguredDamageCondition<?,?>> damageCondition,
											 Holder<ConfiguredBiEntityCondition<?,?>> biEntityCondition,
											 Holder<ConfiguredEntityAction<?,?>> selfAction,
											 Holder<ConfiguredEntityAction<?,?>> targetAction,
											 Holder<ConfiguredBiEntityAction<?, ?>> biEntityAction) implements IValueModifyingPowerConfiguration {

	public ModifyDamageTakenConfiguration(AttributeModifier... modifiers) {
		this(ListConfiguration.of(modifiers), null, null, null, null, null);
	}

	public static final Codec<ModifyDamageTakenConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyDamageTakenConfiguration::modifiers),
			ConfiguredDamageCondition.optional("damage_condition").forGetter(ModifyDamageTakenConfiguration::damageCondition),
			ConfiguredBiEntityCondition.optional("bientity_condition").forGetter(ModifyDamageTakenConfiguration::biEntityCondition),
			ConfiguredEntityAction.optional("self_action").forGetter(ModifyDamageTakenConfiguration::selfAction),
			ConfiguredEntityAction.optional("attacker_action").forGetter(ModifyDamageTakenConfiguration::targetAction),
			ConfiguredBiEntityAction.optional("bientity_action").forGetter(ModifyDamageTakenConfiguration::biEntityAction)
	).apply(instance, ModifyDamageTakenConfiguration::new));
}
