package io.github.apace100.apoli.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.configuration.RaycastSettingsConfiguration;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

import javax.annotation.Nullable;
import java.util.Optional;

public record RaycastConfiguration(RaycastSettingsConfiguration settings,
								   @Nullable ConfiguredEntityAction<?, ?> beforeAction,
								   @Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition,
								   CommandInfo commandInfo, HitAction action) implements IDynamicFeatureConfiguration {
	public static final Codec<RaycastConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			RaycastSettingsConfiguration.MAP_CODEC.forGetter(RaycastConfiguration::settings),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "before_action").forGetter(x -> Optional.ofNullable(x.beforeAction())),
			CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition").forGetter(x -> Optional.ofNullable(x.biEntityCondition())),
			CommandInfo.MAP_CODEC.forGetter(RaycastConfiguration::commandInfo),
			HitAction.MAP_CODEC.forGetter(RaycastConfiguration::action)
	).apply(instance, (t1, t6, t7, t8, t9) -> new RaycastConfiguration(t1, t6.orElse(null), t7.orElse(null), t8, t9)));

	public record CommandInfo(@Nullable String commandAtHit, @Nullable Double commandHitOffset,
							  @Nullable String commandAlongRay,
							  double commandStep,
							  boolean commandAlongRayOnlyOnHit) implements IDynamicFeatureConfiguration {
		private static final MapCodec<CommandInfo> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				CalioCodecHelper.optionalField(Codec.STRING, "command_at_hit").forGetter(x -> Optional.ofNullable(x.commandAtHit())),
				CalioCodecHelper.optionalField(Codec.DOUBLE, "command_hit_offset").forGetter(x -> Optional.ofNullable(x.commandHitOffset())),
				CalioCodecHelper.optionalField(Codec.STRING, "command_along_ray").forGetter(x -> Optional.ofNullable(x.commandAlongRay())),
				CalioCodecHelper.optionalField(Codec.DOUBLE, "command_step", 1.0).forGetter(CommandInfo::commandStep),
				CalioCodecHelper.optionalField(Codec.BOOL, "command_along_ray_only_on_hit", false).forGetter(CommandInfo::commandAlongRayOnlyOnHit)
		).apply(instance, (t1, t2, t3, t4, t5) -> new CommandInfo(t1.orElse(null), t2.orElse(null), t3.orElse(null), t4, t5)));
	}

	public record HitAction(@Nullable ConfiguredBlockAction<?, ?> blockAction,
							@Nullable ConfiguredEntityAction<?, ?> hitAction,
							@Nullable ConfiguredEntityAction<?, ?> missAction,
							@Nullable ConfiguredBiEntityAction<?, ?> biEntityAction) implements IDynamicFeatureConfiguration {
		private static final MapCodec<HitAction> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				CalioCodecHelper.optionalField(ConfiguredBlockAction.CODEC, "block_action").forGetter(x -> Optional.ofNullable(x.blockAction())),
				CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "hit_action").forGetter(x -> Optional.ofNullable(x.hitAction())),
				CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "miss_action").forGetter(x -> Optional.ofNullable(x.missAction())),
				CalioCodecHelper.optionalField(ConfiguredBiEntityAction.CODEC, "bientity_action").forGetter(x -> Optional.ofNullable(x.biEntityAction()))
		).apply(instance, (t1, t2, t3, t4) -> new HitAction(t1.orElse(null), t2.orElse(null), t3.orElse(null), t4.orElse(null))));
	}
}
