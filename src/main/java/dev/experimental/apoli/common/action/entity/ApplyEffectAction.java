package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.configuration.ListConfiguration;
import dev.experimental.apoli.api.power.factory.EntityAction;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

public class ApplyEffectAction extends EntityAction<ListConfiguration<StatusEffectInstance>> {

	public ApplyEffectAction() {
		super(ListConfiguration.codec(SerializableDataTypes.STATUS_EFFECT_INSTANCE, "effect", "effects"));
	}

	@Override
	public void execute(ListConfiguration<StatusEffectInstance> configuration, Entity entity) {
		if (!(entity instanceof LivingEntity) || !entity.getEntityWorld().isClient())
			return;
		for (StatusEffectInstance effect : configuration.getContent())
			((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(effect));
	}
}
