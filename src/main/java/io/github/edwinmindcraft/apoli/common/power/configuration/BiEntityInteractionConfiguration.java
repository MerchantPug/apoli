package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.InteractionPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record BiEntityInteractionConfiguration(@Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition,
											   @Nullable ConfiguredBiEntityAction<?, ?> biEntityAction,
											   InteractionPowerConfiguration interaction) implements IDynamicFeatureConfiguration {

	public static Codec<BiEntityInteractionConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition").forGetter(x -> Optional.ofNullable(x.biEntityCondition())),
			CalioCodecHelper.optionalField(ConfiguredBiEntityAction.CODEC, "bientity_action").forGetter(x -> Optional.ofNullable(x.biEntityAction())),
			InteractionPowerConfiguration.MAP_CODEC.forGetter(BiEntityInteractionConfiguration::interaction)
	).apply(instance, (t1, t2, t3) -> new BiEntityInteractionConfiguration(t1.orElse(null), t2.orElse(null), t3)));

	public static Codec<BiEntityInteractionConfiguration> PREVENTING_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition").forGetter(x -> Optional.ofNullable(x.biEntityCondition())),
			CalioCodecHelper.optionalField(ConfiguredBiEntityAction.CODEC, "bientity_action").forGetter(x -> Optional.ofNullable(x.biEntityAction())),
			InteractionPowerConfiguration.PREVENTING_MAP_CODEC.forGetter(BiEntityInteractionConfiguration::interaction)
	).apply(instance, (t1, t2, t3) -> new BiEntityInteractionConfiguration(t1.orElse(null), t2.orElse(null), t3)));

	public boolean check(Entity actor, Entity target, InteractionHand hand, ItemStack held) {
		return this.interaction().appliesTo(actor.level, hand, held) && ConfiguredBiEntityCondition.check(this.biEntityCondition(), actor, target);
	}

	public InteractionResult executeAction(Entity actor, Entity target, InteractionHand hand) {
		ConfiguredBiEntityAction.execute(this.biEntityAction(), actor, target);
		if (actor instanceof LivingEntity living)
			this.interaction().performActorItemStuff(living, hand);
		return this.interaction().actionResult();
	}
}
