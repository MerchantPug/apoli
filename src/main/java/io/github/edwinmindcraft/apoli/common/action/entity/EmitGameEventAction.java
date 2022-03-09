package io.github.edwinmindcraft.apoli.common.action.entity;

import com.mojang.serialization.Codec;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;

public class EmitGameEventAction extends EntityAction<FieldConfiguration<GameEvent>> {
	public EmitGameEventAction() {
		super(FieldConfiguration.codec(SerializableDataTypes.GAME_EVENT, "event"));
	}

	@Override
	public void execute(FieldConfiguration<GameEvent> configuration, Entity entity) {
		entity.gameEvent(configuration.value());
	}
}
