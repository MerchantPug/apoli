package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record PreventSleepConfiguration(@Nullable ConfiguredBlockCondition<?, ?> condition, String message,
										boolean allowSpawn) implements IDynamicFeatureConfiguration {
	public static final Codec<PreventSleepConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredBlockCondition.CODEC, "block_condition").forGetter(x -> Optional.ofNullable(x.condition())),
			CalioCodecHelper.optionalField(Codec.STRING, "message", "origins.cant_sleep").forGetter(PreventSleepConfiguration::message),
			CalioCodecHelper.optionalField(Codec.BOOL, "set_spawn_point", false).forGetter(PreventSleepConfiguration::allowSpawn)
	).apply(instance, (t1, t2, t3) -> new PreventSleepConfiguration(t1.orElse(null), t2, t3)));
}
