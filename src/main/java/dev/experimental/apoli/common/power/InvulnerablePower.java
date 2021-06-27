package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredDamageCondition;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;

public class InvulnerablePower extends PowerFactory<FieldConfiguration<ConfiguredDamageCondition<?, ?>>> {

	public static boolean isInvulnerableTo(Entity entity, DamageSource source) {
		return IPowerContainer.getPowers(entity, ModPowers.INVULNERABILITY.get()).stream().anyMatch(x -> x.getConfiguration().value().check(source, Float.NaN));
	}

	public InvulnerablePower() {
		super(FieldConfiguration.codec(ConfiguredDamageCondition.CODEC, "damage_condition"));
	}
}
