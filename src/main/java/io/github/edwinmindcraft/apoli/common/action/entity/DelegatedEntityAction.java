package io.github.edwinmindcraft.apoli.common.action.entity;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.common.action.meta.IDelegatedActionConfiguration;
import net.minecraft.world.entity.Entity;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;

public class DelegatedEntityAction<T extends IDelegatedActionConfiguration<Entity>> extends EntityAction<T> {
	public DelegatedEntityAction(Codec<T> codec) {
		super(codec);
	}

	@Override
	public void execute(T configuration, Entity entity) {
		configuration.execute(entity);
	}
}
