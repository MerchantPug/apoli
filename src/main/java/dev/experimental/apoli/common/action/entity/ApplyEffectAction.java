package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.configuration.ListConfiguration;
import dev.experimental.apoli.api.power.factory.EntityAction;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ApplyEffectAction extends EntityAction<ListConfiguration<MobEffectInstance>> {

	public ApplyEffectAction() {
		super(ListConfiguration.codec(SerializableDataTypes.STATUS_EFFECT_INSTANCE, "effect", "effects"));
	}

	@Override
	public void execute(ListConfiguration<MobEffectInstance> configuration, Entity entity) {
		if (!(entity instanceof LivingEntity) || !entity.getCommandSenderWorld().isClientSide())
			return;
		for (MobEffectInstance effect : configuration.getContent())
			((LivingEntity) entity).addEffect(new MobEffectInstance(effect));
	}
}
