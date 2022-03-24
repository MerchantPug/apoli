package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.TagConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record PreventGameEventConfiguration(@Nullable TagConfiguration<GameEvent> tag,
											ListConfiguration<GameEvent> list,
											Holder<ConfiguredEntityAction<?, ?>> action) implements IDynamicFeatureConfiguration {
	public static final Codec<PreventGameEventConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			TagConfiguration.optionalField(SerializableDataTypes.GAME_EVENT_TAG, "tag").forGetter(x -> Optional.ofNullable(x.tag())),
			ListConfiguration.mapCodec(SerializableDataTypes.GAME_EVENT, "event", "events").forGetter(PreventGameEventConfiguration::list),
			ConfiguredEntityAction.optional("entity_action").forGetter(PreventGameEventConfiguration::action)
	).apply(instance, (t1, t2, t3) -> new PreventGameEventConfiguration(t1.orElse(null), t2, t3)));

	public boolean doesPrevent(GameEvent event) {
		if (this.tag() != null && event.is(this.tag().value()))
			return true;
		return this.list().entries().contains(event);
	}
}
