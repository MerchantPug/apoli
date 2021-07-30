package dev.experimental.apoli.common.condition.entity;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.common.condition.meta.IDelegatedConditionConfiguration;
import net.minecraft.world.entity.LivingEntity;

public class DelegatedEntityCondition<T extends IDelegatedConditionConfiguration<LivingEntity>> extends EntityCondition<T> {
	public DelegatedEntityCondition(Codec<T> codec) {
		super(codec);
	}

	@Override
	public boolean check(T configuration, LivingEntity entity) {
		return configuration.check(entity);
	}
}
