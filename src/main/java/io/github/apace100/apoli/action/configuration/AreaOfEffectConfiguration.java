package io.github.apace100.apoli.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

import javax.annotation.Nullable;
import java.util.Optional;

public record AreaOfEffectConfiguration(double radius, ConfiguredBiEntityAction<?, ?> action,
										@Nullable ConfiguredBiEntityCondition<?, ?> condition, boolean includeTarget) implements IDynamicFeatureConfiguration {
	public static final Codec<AreaOfEffectConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(Codec.DOUBLE, "radius", 16.0).forGetter(AreaOfEffectConfiguration::radius),
			ConfiguredBiEntityAction.CODEC.fieldOf("bientity_action").forGetter(AreaOfEffectConfiguration::action),
			CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition").forGetter(x -> Optional.ofNullable(x.condition())),
			CalioCodecHelper.optionalField(Codec.BOOL, "include_target", false).forGetter(AreaOfEffectConfiguration::includeTarget)
	).apply(instance, (t1, t2, t3, t4) -> new AreaOfEffectConfiguration(t1, t2, t3.orElse(null), t4)));
}
