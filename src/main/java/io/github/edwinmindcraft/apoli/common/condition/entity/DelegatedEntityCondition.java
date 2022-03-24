package io.github.edwinmindcraft.apoli.common.condition.entity;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.meta.IDelegatedConditionConfiguration;
import net.minecraft.world.entity.Entity;

public class DelegatedEntityCondition<T extends IDelegatedConditionConfiguration<Entity>> extends EntityCondition<T> {
	public DelegatedEntityCondition(Codec<T> codec) {
		super(codec);
	}

	@Override
	public boolean check(T configuration, Entity entity) {
		return configuration.check(entity);
	}
}
