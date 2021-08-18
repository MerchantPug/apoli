package dev.experimental.apoli.common.condition.damage;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityCondition;
import dev.experimental.apoli.api.power.factory.DamageCondition;
import java.util.Optional;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class AttackerCondition extends DamageCondition<FieldConfiguration<Optional<ConfiguredEntityCondition<?, ?>>>> {

	public AttackerCondition() {
		super(FieldConfiguration.optionalCodec(ConfiguredEntityCondition.CODEC, "entity_condition"));
	}

	@Override
	protected boolean check(FieldConfiguration<Optional<ConfiguredEntityCondition<?, ?>>> configuration, DamageSource source, float amount) {
		Entity attacker = source.getEntity();
		return attacker instanceof LivingEntity le ? configuration.value().map(x -> x.check(le)).orElse(true) : Boolean.valueOf(false);
	}
}
