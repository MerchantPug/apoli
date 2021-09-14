package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.apace100.calio.data.SerializableDataTypes;
import java.util.Optional;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ClearEffectAction extends EntityAction<FieldConfiguration<Optional<MobEffect>>> {

	public ClearEffectAction() {
		super(FieldConfiguration.optionalCodec(SerializableDataTypes.STATUS_EFFECT, "effect"));
	}

	@Override
	public void execute(FieldConfiguration<Optional<MobEffect>> configuration, Entity entity) {
		if (entity instanceof LivingEntity living)
			configuration.value().ifPresentOrElse(living::removeEffect, living::removeAllEffects);
	}
}
