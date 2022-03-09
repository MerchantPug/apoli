package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.TagConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record PreventGameEventConfiguration(@Nullable TagConfiguration<GameEvent> tag,
											ListConfiguration<GameEvent> list,
											@Nullable ConfiguredEntityAction<?, ?> action) implements IDynamicFeatureConfiguration {
	public static final Codec<PreventGameEventConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			TagConfiguration.optionalField(SerializableDataTypes.GAME_EVENT_TAG, "tag").forGetter(x -> Optional.ofNullable(x.tag())),
			ListConfiguration.mapCodec(SerializableDataTypes.GAME_EVENT, "event", "events").forGetter(PreventGameEventConfiguration::list),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action").forGetter(x -> Optional.ofNullable(x.action()))
	).apply(instance, (t1, t2, t3) -> new PreventGameEventConfiguration(t1.orElse(null), t2, t3.orElse(null))));

	public boolean doesPrevent(GameEvent event) {
		if (this.tag() != null && this.tag().contains(event))
			return true;
		return this.list() != null && this.list().entries().contains(event);
	}
}
