package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

import javax.annotation.Nullable;
import java.util.Optional;

public record VehicleActionConfiguration(@Nullable ConfiguredEntityAction<?, ?> action,
										 @Nullable ConfiguredBiEntityAction<?, ?> biEntityAction,
										 @Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition,
										 boolean recursive) implements IDynamicFeatureConfiguration {
	public static final Codec<VehicleActionConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "action").forGetter(x -> Optional.ofNullable(x.action())),
			CalioCodecHelper.optionalField(ConfiguredBiEntityAction.CODEC, "bientity_action").forGetter(x -> Optional.ofNullable(x.biEntityAction())),
			CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition").forGetter(x -> Optional.ofNullable(x.biEntityCondition())),
			CalioCodecHelper.optionalField(Codec.BOOL, "recursive", false).forGetter(VehicleActionConfiguration::recursive)
	).apply(instance, (t1, t2, t3, t4) -> new VehicleActionConfiguration(t1.orElse(null), t2.orElse(null), t3.orElse(null), t4)));

}
