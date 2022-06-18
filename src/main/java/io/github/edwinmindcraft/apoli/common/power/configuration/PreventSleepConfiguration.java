package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record PreventSleepConfiguration(Holder<ConfiguredBlockCondition<?,?>> condition, String message,
										boolean allowSpawn) implements IDynamicFeatureConfiguration {
	public static final Codec<PreventSleepConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredBlockCondition.optional("block_condition").forGetter(PreventSleepConfiguration::condition),
			CalioCodecHelper.optionalField(Codec.STRING, "message", "origins.cant_sleep").forGetter(PreventSleepConfiguration::message),
			CalioCodecHelper.optionalField(CalioCodecHelper.BOOL, "set_spawn_point", false).forGetter(PreventSleepConfiguration::allowSpawn)
	).apply(instance, PreventSleepConfiguration::new));
}
