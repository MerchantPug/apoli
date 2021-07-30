package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.EntityAction;
import io.github.apace100.calio.data.SerializableDataTypes;
import java.util.Optional;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;

public class ClearEffectAction extends EntityAction<FieldConfiguration<Optional<MobEffect>>> {

	public ClearEffectAction() {
		super(FieldConfiguration.optionalCodec(SerializableDataTypes.STATUS_EFFECT, "effect"));
	}

	@Override
	public void execute(FieldConfiguration<Optional<MobEffect>> configuration, Entity entity) {
		if (entity instanceof LivingEntity living)
			configuration.value().ifPresentOrElse(living::removeStatusEffect, living::clearStatusEffects);
	}
}
