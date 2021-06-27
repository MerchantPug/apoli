package dev.experimental.apoli.common.action.entity;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.common.action.meta.IDelegatedActionConfiguration;
import dev.experimental.apoli.api.power.factory.EntityAction;
import net.minecraft.entity.Entity;

public class DelegatedEntityAction<T extends IDelegatedActionConfiguration<Entity>> extends EntityAction<T> {
	public DelegatedEntityAction(Codec<T> codec) {
		super(codec);
	}

	@Override
	public void execute(T configuration, Entity entity) {
		configuration.execute(entity);
	}
}
