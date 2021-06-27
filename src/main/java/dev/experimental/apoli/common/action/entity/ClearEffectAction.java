package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.EntityAction;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;

import java.util.Optional;

public class ClearEffectAction extends EntityAction<FieldConfiguration<Optional<StatusEffect>>> {

	public ClearEffectAction() {
		super(FieldConfiguration.optionalCodec(SerializableDataTypes.STATUS_EFFECT, "effect"));
	}

	@Override
	public void execute(FieldConfiguration<Optional<StatusEffect>> configuration, Entity entity) {
		if (entity instanceof LivingEntity living)
			configuration.value().ifPresentOrElse(living::removeStatusEffect, living::clearStatusEffects);
	}
}
